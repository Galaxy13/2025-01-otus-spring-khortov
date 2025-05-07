package com.galaxy13.hw.exception;

public class MismatchedIdsException extends RuntimeException {
  public MismatchedIdsException(Long pathId, Long bodyId) {
    super(String.format("ID in path (%d) doesn't match ID in body (%d)", pathId, bodyId));
  }
}
