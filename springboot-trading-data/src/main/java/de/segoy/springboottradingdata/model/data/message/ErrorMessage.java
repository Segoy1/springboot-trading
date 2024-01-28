package de.segoy.springboottradingdata.model.data.message;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage extends BaseMessage {

    private Integer messageId;

    private String message;

    private Integer errorCode;
    private String advancedOrderReject;
}
