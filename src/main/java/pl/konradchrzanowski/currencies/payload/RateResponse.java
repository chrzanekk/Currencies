package pl.konradchrzanowski.currencies.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {

    @JsonProperty("mid")
    private String mid;

    @JsonProperty("code")
    private String code;
}
