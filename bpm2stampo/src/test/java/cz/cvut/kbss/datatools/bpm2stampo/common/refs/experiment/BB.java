package cz.cvut.kbss.datatools.bpm2stampo.common.refs.experiment;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;

import java.util.Objects;

public class BB {
    public String id;
    @FIDAttribute(cls = A.class, fieldRef = "name")
    public String name1;
    @FIDAttribute(fieldRef = "secondName")
    public String lastName1;
    // Implementation from 20.8.2019 does not support this feature. Possible solution is to use a foreign-key/key-mapping
    // identifier.
    @FIDAttribute(cls = A.class, fieldRef = "name", value = "1")
    public String name2;
    @FIDAttribute(fieldRef = "secondName", value = "1")
    public String lastName2;

    @Relation
    public A a1;
    @Relation("1")
    public A a2;

    public BB(String id, String name1, String lastName1, String name2, String lastName2) {
        this.id = id;
        this.name1 = name1;
        this.lastName1 = lastName1;
        this.name2 = name2;
        this.lastName2 = lastName2;
    }

    @Override
    public String toString() {
        return "BB{" +
                "id='" + id + '\'' + "\n" +
                "\n\tname1='" + name1 + '\'' + "\n" +
                "\n\tlastName1='" + lastName1 + '\'' + "\n" +
                "\n\tname2='" + name2 + '\'' + "\n" +
                "\n\tlastName2='" + lastName2 + '\'' + "\n" +
                "\n\ta1='" + a1 + '\'' + "\n" +
                "\n\ta2='" + a2 + '\'' + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BB bb = (BB) o;
        return Objects.equals(id, bb.id) &&
                Objects.equals(name1, bb.name1) &&
                Objects.equals(lastName1, bb.lastName1) &&
                Objects.equals(name2, bb.name2) &&
                Objects.equals(lastName2, bb.lastName2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name1, lastName1, name2, lastName2);
    }
}
