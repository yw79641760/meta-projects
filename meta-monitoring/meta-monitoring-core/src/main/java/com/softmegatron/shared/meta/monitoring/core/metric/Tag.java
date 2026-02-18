package com.softmegatron.shared.meta.monitoring.core.metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 指标标签
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class Tag {

    private final String key;
    private final String value;

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Tag of(String key, String value) {
        return new Tag(key, value);
    }

    public static List<Tag> concat(List<Tag> tags, Tag tag) {
        List<Tag> result = new ArrayList<>(tags);
        result.add(tag);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(key, tag.key) && Objects.equals(value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
