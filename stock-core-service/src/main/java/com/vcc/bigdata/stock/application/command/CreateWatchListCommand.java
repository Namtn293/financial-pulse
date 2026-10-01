package com.vcc.bigdata.stock.application.command;

import java.util.List;

public record CreateWatchListCommand (
   Long userId,
   String name,
   List<CreateWatchListItemCommand> items
) {}
