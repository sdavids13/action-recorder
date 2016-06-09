package com.sdavids.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Action implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Verb verb;

    @NotEmpty
    @Column(nullable = false, updatable = false)
    private String objectUri;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ObjectType objectType;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createDate;

    protected Action() {
        //Required for JPA
    }

    public Action(Verb verb, ObjectType objectType, String objectUri) {
        this.verb = verb;
        this.objectType = objectType;
        this.objectUri = objectUri;
    }

    public Long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Verb getVerb() {
        return verb;
    }

    public String getObjectUri() {
        return objectUri;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public Date getCreateDate() {
        return createDate;
    }

}
