package com.kenny.poc.atp.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommonMessage {

    private final LocalDateTime curruentDateTime;
}
