package tickhubs.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import tickhubs.enums.PollType;
import tickhubs.model.Choice;
import tickhubs.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PollDTO {

    Long id;
    String question;
    List<Choice> choices = new ArrayList<>();
    Instant expirationDateTime;
    Long createdById;
    String createdByUserName;
    PollType type;
}
