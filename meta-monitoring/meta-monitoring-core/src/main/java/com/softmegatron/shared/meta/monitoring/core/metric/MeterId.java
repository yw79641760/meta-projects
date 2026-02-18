package com.softmegatron.shared.meta.monitoring.core.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 指标ID
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MeterId {

    private final String name;
    private final List<Tag> tags;

    public MeterId(String name) {
        this(name, Collections.emptyList());
    }

    public MeterId(String name, Tag... tags) {
        this(name, Arrays.asList(tags));
    }

    public MeterId(String name, List<Tag> tags) {
        this.name = name;
        this.tags = tags != null ? Collections.unmodifiableList(tags) : Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public MeterId withTag(String key, String value) {
        return new MeterId(name, Tag.concat(tags, new Tag(key, value)));
    }

    public String getTag(String key) {
        return tags.stream()
                .filter(t -> t.getKey().equals(key))
                .map(Tag::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        if (!tags.isEmpty()) {
            sb.append("[");
            for (int i = 0; i < tags.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(tags.get(i).getKey()).append("=").append(tags.get(i).getValue());
            }
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterId meterId = (MeterId) o;
        return name.equals(meterId.name) && tags.equals(meterId.tags);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + tags.hashCode();
        return result;
    }
}
