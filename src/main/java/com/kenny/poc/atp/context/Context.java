package com.kenny.poc.atp.context;

import lombok.Builder;

@Builder
public record Context(String guid, String userId) {
}
