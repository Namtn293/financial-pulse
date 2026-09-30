---
name: backend
description: >-
  Các pattern phát triển backend Java Spring Boot: cấu trúc package theo Clean/Hexagonal Architecture, controller, service, repository, DTO, dependency injection, xử lý lỗi, và bắt buộc envelope phân trang items+meta cho API list/search. Áp dụng khi chỉnh sửa bất kỳ file Java nào trong src/main/java/**, viết hoặc sửa controller, service, repository, entity, DTO, API list/search, hoặc khi đề cập đến Spring, Maven, JPA, Hibernate, hay Java backend.
---

# Phát triển Backend Java Spring Boot

## Cấu trúc Package & Module

Tuân thủ kiến trúc Clean/Hexagonal Architecture (Ports and Adapters) với cấu trúc package rõ ràng:

```text
layer/
├── domain/               # Core Domain Layer (No framework dependencies)
│   ├── model/            # Pure Java Domain Models (e.g., User.java, ApiKey.java)
│   │   └── enums/        # Domain enums
│   ├── exception/        # Business exceptions (e.g., BusinessRuleException.java)
│   └── event/            # Domain events
├── application/          # Use Case Orchestration Layer (Pure business rules)
│   ├── port/             # Port interfaces
│   │   ├── inbound/      # Use case interfaces (e.g., ProfileUseCase.java)
│   │   └── outbound/     # Gateway interfaces (e.g., UserRepositoryPort.java)
│   ├── service/          # Application Services implementing inbound ports (e.g., ProfileService.java)
│   ├── dto/              # Application-level DTOs (e.g., PaginatedResult.java)
│   ├── strategy/         # Business strategies
│   └── listener/         # Event listeners
└── infrastructure/       # Technical Details Layer (Frameworks, DB, External APIs, Config)
    ├── config/           # General configurations (Security, Swagger, Web, Beans)
    ├── security/         # Security logic (JWT validation, filters)
    ├── bootstrap/        # Startup runners and database initializers
    └── adapter/          # Tech adapters implementing ports or exposing endpoints
        ├── inbound/      # Driving Adapters (Web controller, scheduler)
        │   └── web/      # Spring Web/REST API Layer
        │       ├── controller/ # REST Endpoints (e.g., WorkspaceController.java)
        │       ├── dto/        # Web Request/Response DTOs
        │       ├── mapper/     # MapStruct Web Mappers
        │       └── handler/    # Global Exception Handler Advice
        └── outbound/     # Driven Adapters (Persistence, notification, mail)
            └── persistence/    # DB Persistence Adapter implementing Outbound Ports
                ├── jpa/        # Spring Data JPA
                │   ├── entity/ # DB Table Mapped Entities (e.g., UserEntity.java)
                │   └── repository/ # JpaRepository Interfaces (e.g., UserJpaRepository.java)
                └── mapper/     # MapStruct Entity-Domain converters
```

### Quy ước Đặt tên File & Class

- **Inbound Port / Use Case**: `[Feature]UseCase.java` (VD: `ProfileUseCase.java`)
- **Application Service**: `[Feature]Service.java` (VD: `ProfileService.java` triển khai `ProfileUseCase`)
- **Outbound Port**: `[Feature]RepositoryPort.java` hoặc `[Feature][Gateway]Port.java` (VD: `UserRepositoryPort.java`, `MailNotificationSenderPort.java`)
- **Outbound Adapter**: `[Feature]PersistenceAdapter.java` hoặc `[Feature][Gateway]Adapter.java` (VD: `UserPersistenceAdapter.java` triển khai `UserRepositoryPort`)
- **JPA Repository**: `[Feature]JpaRepository.java` (VD: `UserJpaRepository.java`)
- **JPA Entity**: `[Feature]Entity.java` (VD: `UserEntity.java`)
- **Domain Model**: `[Feature].java` (VD: `User.java`)
- **Web Controller**: `[Feature]Controller.java` (VD: `WorkspaceController.java`)
- **Web DTO**: `[Feature]Request.java`, `[Feature]Response.java` (trong thư mục web/dto)
- **Mapper**:
  - `[Feature]PersistenceMapper.java` (ánh xạ database: entity ↔ domain)
  - `[Feature]WebMapper.java` hoặc `[Feature]Mapper.java` (ánh xạ web DTO: DTO ↔ domain)

---

## Dependency Injection & Lombok

### 1. Constructor Injection

Không sử dụng field injection (`@Autowired` trực tiếp trên field). Thay vào đó, dùng constructor injection kết hợp với `@RequiredArgsConstructor` của Lombok để tự sinh constructor cho các dependency `final`:

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CampaignService implements CampaignUseCase {

    private final CampaignRepositoryPort campaignRepositoryPort;

    @Override
    public Campaign getCampaignById(UUID id) {
        log.info("Fetching campaign with id: {}", id);
        return campaignRepositoryPort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Campaign not found with ID: " + id));
    }
}
```

### 2. Quy tắc sử dụng Lombok

- **Sử dụng Lombok** để giảm boilerplate: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Slf4j`.
- **CẢNH BÁO với JPA Entity**: Tránh dùng `@Data` hoặc `@EqualsAndHashCode` trên các class JPA Entity. Điều này có thể gây vòng lặp vô hạn với quan hệ hai chiều hoặc lỗi với Hibernate lazy loading. Thay vào đó, dùng `@Getter` và `@Setter` riêng lẻ, và viết `equals()` / `hashCode()` thủ công dựa trên natural ID nếu cần.

---

## REST Controller & DTO

### 1. REST Controller

- Đánh dấu với `@RestController` và `@RequestMapping("/api/v1/...")`.
- Sử dụng annotation mapping cụ thể: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`.
- Trả về `ResponseEntity<T>` với cấu trúc response envelope chuẩn của dự án (nếu có).
- Giữ controller mỏng; ủy quyền toàn bộ logic nghiệp vụ cho application service.

### 2. API list / search — bắt buộc envelope phân trang

**Bắt buộc:** mọi endpoint **list** hoặc **search** trả về danh sách có phân trang (`page`/`size` hoặc tương đương) **không** được trả `data` là mảng phẳng. `data` phải theo cấu trúc:

```json
{
  "data": {
    "items": [ /* ... */ ],
    "meta": {
      "totalItems": 0,
      "itemCount": 0,
      "itemsPerPage": 10,
      "totalPages": 0,
      "currentPage": 1
    }
  }
}
```

| Layer | Quy tắc |
|-------|---------|
| UseCase / Service / RepositoryPort | Trả `PaginatedResult<T>` (có `totalElements`, `totalPages`, `pageNumber`, `pageSize`) — không trả `List` thuần |
| Controller | Map sang `PaginatedResponse<T>` rồi `apiResponseFactory.paginated(...)` |
| Docs API | Ví dụ response list/search phải có `items` + `meta`; không mô tả thừa trước block JSON |
| Ngoại lệ | Catalog/metadata không phân trang (dimensions, metrics, platform accounts…) được trả `List` |

```java
@GetMapping
public ResponseEntity<BaseApiResponse<PaginatedResponse<ItemResponse>>> list(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        /* ... */) {
    PaginationQuery paginationQuery = PaginationQuery.of(page, size, sortBy, sortDirection);
    PaginatedResult<Item> result = useCase.list(..., paginationQuery);
    PaginatedResponse<ItemResponse> response =
            PaginatedResponse.from(result, webMapper::toResponse);
    return ResponseEntity.ok(apiResponseFactory.paginated(response, httpRequest));
}
```

**Cấm:** `apiResponseFactory.success(list, httpRequest)` khi `list` là `List<?>` của endpoint list/search phân trang.

### 3. Validation trên DTO

Sử dụng Jakarta Validation (`jakarta.validation.constraints.*`) để kiểm tra payload đầu vào:

- `@NotNull`, `@NotBlank` (cho String), `@NotEmpty`
- `@Size(min = ..., max = ...)`
- `@Min`, `@Max`, `@Positive`, `@PositiveOrZero`
- `@Email`
- Luôn đánh dấu `@Valid` trên request body trong Controller để kích hoạt validation.

---

## MapStruct Mapper

Sử dụng MapStruct để chuyển đổi giữa các model và DTO/Entity.

- Định nghĩa mapper dưới dạng interface với `@Mapper(componentModel = "spring")`.
- Ánh xạ tên field không khớp bằng `@Mapping(source = "srcField", target = "targetField")`.

Ví dụ Web DTO Mapper:

```java
@Mapper(componentModel = "spring")
public interface CampaignWebMapper {
    CampaignResponse toResponse(Campaign campaign);
    Campaign toDomain(CampaignRequest request);
}
```

Ví dụ Persistence Entity Mapper:

```java
@Mapper(componentModel = "spring")
public interface CampaignPersistenceMapper {
    Campaign toDomain(CampaignEntity entity);
    CampaignEntity toEntity(Campaign domain);
}
```

---

## Xử lý Exception & Lỗi

### 1. Global Exception Handler

Sử dụng `@RestControllerAdvice` để bắt exception toàn cục và trả về cấu trúc response nhất quán:

```java
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
            ));
    }
}
```

---

## Checklist

- [ ] Các method của Controller trả về `ResponseEntity<T>` với HTTP status code phù hợp.
- [ ] API list/search phân trang trả `data: { items, meta }` qua `PaginatedResponse` — không trả mảng phẳng.
- [ ] Dependency injection sử dụng Constructor Injection (không dùng `@Autowired` trên field).
- [ ] DTO request có annotation validation, Controller sử dụng `@Valid`.
- [ ] JPA Entity class không dùng `@Data` (dùng `@Getter` và `@Setter` riêng).
- [ ] Xử lý exception tập trung trong class `@RestControllerAdvice`.
- [ ] MapStruct được dùng cho chuyển đổi object thay vì mapping thủ công.
- [ ] Sử dụng Lombok `@Slf4j` để ghi log thay vì `System.out.println` hoặc tạo Logger thủ công.
- [ ] Áp dụng quy tắc format code (Spotless / Google Java Format) cho tất cả class mới.
- [ ] API endpoint được tài liệu hóa bằng OpenAPI annotation (`@Tag`, `@Operation`...).
