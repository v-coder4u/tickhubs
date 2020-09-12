package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.dom4j.tree.BaseElement;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileManager extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @NotNull(message = "{NotNull.FileManager.name}")
    String name;

    @NotNull(message = "{NotNull.FileManager.mimeType}")
    String mimeType;

    @NotNull(message = "{NotNull.FileManager.s3Url}")
    @Lob
    String s3Url;

    @NotNull(message = "{NotNull.FileManager.s3FileName}")
    String s3FileName;
}
