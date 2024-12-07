package de.segoy.springboottradingdata.model.data;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public abstract class IBKRDataType extends BaseEntity{

    // Custom equals method for POJOs with any number of fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;  // Same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Different class or null object
        }

        // Use reflection to compare fields
        try {
            Field[] fields = getClass().getDeclaredFields();  // Get all fields in the current class
            for (Field field : fields) {
                field.setAccessible(true);  // Ensure private fields can be accessed

                Object thisValue = field.get(this);  // Get field value from this object
                Object otherValue = field.get(obj);  // Get field value from the other object

                // Compare the values of the fields
                if (!Objects.equals(thisValue, otherValue)) {
                    return false;  // Return false if any field is different
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();  // Handle possible reflection access exceptions
            return false;
        }

        return true;  // All fields are equal
    }

    @Override
    public int hashCode() {
        // For consistency with equals(), we need to override hashCode as well
        // Using reflection to generate hashCode based on field values
        int result = 17;  // Start with a non-zero constant
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(this);
                result = 31 * result + (value == null ? 0 : value.hashCode());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
