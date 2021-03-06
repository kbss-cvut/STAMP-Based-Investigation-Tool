package cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.Constants;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD}) // TODO - implement ElementType.METHOD to use for getters and setters instead of the field
@Repeatable(FIDAttributes.class)
public @interface FIDAttribute {
    /** the id/name of the key mapping. Not required when there is only one key-mapping. Must be used in case of multiple key-mappings.*/
    String value() default Constants.NO_ID_NAME;
//    String fkId() default Constants.NO_ID_NAME; // NOT IMPLEMENTED // This is used when a class contains multiple sets of fields defining FKs to the same referenced class.
    /** The name of the field */
    String fieldRef() default Constants.NO_FIELD_REFERENCE;
    Class cls() default void.class;
    RelationTypes relationType() default RelationTypes.oneToOne;

}
