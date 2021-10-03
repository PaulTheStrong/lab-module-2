package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@JsonIgnoreProperties("certificates")
@Table(name="tag")
public class Tag extends Identifiable {

    @NotNull(message = "40008")
    @Size(min = 1, max = 20, message = "40011")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<GiftCertificate> certificates;

    public Tag(Integer id, String name) {
        super(id);
        this.name = name;
        this.certificates = new ArrayList<>();
    }

    public Tag(Integer id, String name, List<GiftCertificate> certificates) {
        super(id);
        this.name = name;
        this.certificates = certificates;
    }

    public Tag(String name, List<GiftCertificate> certificates) {
        this.name = name;
        this.certificates = certificates;
    }

    public Tag(String name) {
        this(null, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
    }
}
