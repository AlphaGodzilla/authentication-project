package io.github.alphagodzilla.authentication.defaults;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class JacksonJsonParserTest {
    /**
     * demo target class
     */
    public static class TargetJsonObject {
        private Long id;
        private String name;

        public TargetJsonObject() {
        }

        public TargetJsonObject(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TargetJsonObject)) return false;
            final TargetJsonObject that = (TargetJsonObject) o;
            return id.equals(that.id) && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    @Test
    public void toBean() {
        String jsonStr = "{\"id\":1,\"name\":\"jackson\"}";
        TargetJsonObject targetJsonObject = new TargetJsonObject(1L, "jackson");
        TargetJsonObject result = new JacksonJsonParser().toBean(jsonStr, TargetJsonObject.class);
        Assert.assertEquals(targetJsonObject, result);
    }

    @Test
    public void toJsonString() {
        String jsonStrOption1 = "{\"id\":1,\"name\":\"jackson\"}";
        String jsonStrOption2 = "{\"name\":\"jackson\",\"id\":1}";
        TargetJsonObject targetJsonObject = new TargetJsonObject(1L, "jackson");
        String result = new JacksonJsonParser().toJsonString(targetJsonObject);
        Assert.assertTrue(result.equals(jsonStrOption1) || result.equals(jsonStrOption2));
    }
}
