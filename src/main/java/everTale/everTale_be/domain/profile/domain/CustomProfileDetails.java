package everTale.everTale_be.domain.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomProfileDetails{
    private final Long userId;
    private final Long profileId;
}

