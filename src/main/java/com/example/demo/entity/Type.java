package com.example.demo.entity;

import com.example.demo.utils.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameType;

    private String slug;
    @PrePersist
    private void generateSlug() {
        if (StringUtils.isEmpty(this.slug)) {
            this.slug = Util.toSlug(nameType);
        }
    }
    @PreUpdate
    private void generateSlugUpdate(){
        this.slug = Util.toSlug(nameType);
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "types")
    private List<Product> products;
}
