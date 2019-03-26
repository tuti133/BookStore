package ptit.htpt.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @NotNull
    private Boolean activated;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String gender;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "account_authority",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )
    private Set<Authority> authorities;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "created_by")
    private Long createdBy;
}
