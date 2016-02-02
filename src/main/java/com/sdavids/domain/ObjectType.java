package com.sdavids.domain;

public enum ObjectType {
    ARTICLE("http://activitystrea.ms/schema/1.0/article"),
    AUDIO("http://activitystrea.ms/schema/1.0/audio"),
    BOOKMARK("http://activitystrea.ms/schema/1.0/bookmark"),
    COMMENT("http://activitystrea.ms/schema/1.0/comment"),
    EVENT("http://activitystrea.ms/schema/1.0/event"),
    FILE("http://activitystrea.ms/schema/1.0/file"),
    FOLDER("http://activitystrea.ms/schema/1.0/folder"),
    NOTE("http://activitystrea.ms/schema/1.0/note"),
    PERSON("http://activitystrea.ms/schema/1.0/person"),
    PHOTO("http://activitystrea.ms/schema/1.0/photo"),
    PLACE("http://activitystrea.ms/schema/1.0/place"),
    STATUS("http://activitystrea.ms/schema/1.0/status"),
    VIDEO("http://activitystrea.ms/schema/1.0/video");

    private final String typeIRI;

    ObjectType(final String typeIRI) {
        this.typeIRI = typeIRI;
    }

    public String getTypeIRI() {
        return typeIRI;
    }

}
