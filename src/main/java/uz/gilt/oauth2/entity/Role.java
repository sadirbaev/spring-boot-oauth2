package uz.gilt.oauth2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    public enum ERole {
        user,
        admin
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;

}
