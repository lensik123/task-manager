package ru.baysarov.task_manager.util;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class EnumUtils {

  public static <E extends Enum<E>> String getEnumValues(Class<E> enumClass) {
    return String.join(", ", EnumSet.allOf(enumClass).stream()
        .map(Enum::name)
        .map(String::toUpperCase)
        .collect(Collectors.toList()));
  }
}
