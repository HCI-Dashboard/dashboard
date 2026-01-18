package kr.co.bnbsoft.dashboard.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DateDTO implements Serializable {
    protected String createdDate;
    protected String createdId;
    protected String modifiedDate;
    protected String modifiedId;
}
