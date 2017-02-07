package com.insightfullogic.honest_profiler.core.aggregation.result.straight;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.insightfullogic.honest_profiler.core.aggregation.AggregationProfile;
import com.insightfullogic.honest_profiler.core.aggregation.filter.FilterSpecification;
import com.insightfullogic.honest_profiler.core.aggregation.grouping.CombinedGrouping;
import com.insightfullogic.honest_profiler.core.aggregation.result.Aggregation;
import com.insightfullogic.honest_profiler.core.profiles.lean.LeanNode;

/**
 * A Flat is a list-based {@link Aggregation}. {@link LeanNode}s are aggregated by using the {@link CombinedGrouping} to
 * extract a String key, and each {@link Entry} in the Flat is the aggregation of all {@link LeanNode}s with the same
 * key.
 */
public class Flat extends Aggregation<Entry>
{
    // Instance Constructors

    /**
     * Create an empty {@link Flat} for the specified {@link AggregationProfile} and {@link CombinedGrouping}.
     *
     * @param source the {@link AggregationProfile} whose {@link LeanNode}s are aggregated into this Flat
     * @param grouping the {@link CombinedGrouping} used for aggregation
     */
    public Flat(AggregationProfile source, CombinedGrouping grouping)
    {
        super(source, grouping, new ArrayList<>());
    }

    /**
     * @see Aggregation#Aggregation(AggregationProfile, CombinedGrouping, List)
     */
    public Flat(AggregationProfile source, CombinedGrouping grouping, List<Entry> data)
    {
        super(source, grouping, data);
    }

    @Override
    public List<Entry> getData()
    {
        return super.getData();
    }

    @Override
    public Flat filter(FilterSpecification<Entry> filterSpec)
    {
        return new Flat(
            getSource(),
            getGrouping(),
            getData().stream().filter(filterSpec.getFilter()).collect(toList()));
    }
}
