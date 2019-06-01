//////////////////////////////////////////////////////////////////////// Package
package jptw.sudoku;

//////////////////////////////////////////////////////////////////////// Imports

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/////////////////////////////////////////////////////////////// Class definition
class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    ////////////////////////////////////////////////////////////////// [Methods]
    //----------------------------------------------------------- Constructors <
    public SudokuField() {
        this(0);
    }

    public SudokuField(final int value) {
        setFieldValue(value);
    }

    //-------------------------------------------------------------- Accessors <
    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(final int value) {
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("value: " + value
                    + " should be within the range of [0, 9]!");
        }
        this.value = value;
    }

    //------------------------------------------------------------- Comparison <
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        SudokuField field = (SudokuField) object;
        return new EqualsBuilder().append(this.value, field.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 17).append(this.value).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("value", this.value).toString();
    }

    @Override
    public int compareTo(final SudokuField o) {

        if (this.getFieldValue() == o.getFieldValue()) {
            return 0;
        } else if (this.getFieldValue() > o.getFieldValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    protected Object clone() {
        return new SudokuField(value);
    }

    /////////////////////////////////////////////////////////////////// [Fields]
    private int value;
}

////////////////////////////////////////////////////////////////////////////////