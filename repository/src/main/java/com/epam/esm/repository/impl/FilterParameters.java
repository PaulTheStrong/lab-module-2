package com.epam.esm.repository.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FilterParameters {

    private Set<String> tags;
    private Map<SortColumn, SortType> sorts;
    private String nameOrDescription;

    private FilterParameters(Set<String> tag, Map<SortColumn, SortType> sorts, String tagOrDescription) {
        this.tags = tag;
        this.sorts = sorts;
        this.nameOrDescription = tagOrDescription;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Map<SortColumn, SortType> getSorts() {
        return sorts;
    }

    public String getNameOrDescription() {
        return nameOrDescription;
    }

    public static FilterParametersBuilder builder() {
        return new FilterParametersBuilder();
    }

    public static class FilterParametersBuilder {
        private Set<String> tags = new HashSet<>();
        private final Map<SortColumn, SortType> sorts = new LinkedHashMap<>();
        private String nameOrDescription;

        public FilterParametersBuilder withTag(String tag) {
            tags.add(tag);
            return this;
        }

        public FilterParametersBuilder withSort(SortColumn sortColumn, SortType sortType) {
            if (sortColumn != SortColumn.NONE && sortType != SortType.NONE) {
                sorts.put(sortColumn, sortType);
            }
            return this;
        }

        public FilterParametersBuilder withNameOrDescription(String nameOrDescription) {
            this.nameOrDescription = nameOrDescription;
            return this;
        }

        public FilterParameters build() {
            return new FilterParameters(tags, sorts, nameOrDescription);
        }
    }
}
