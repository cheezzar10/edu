package com.odin.rnd.edu.jpaperf.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonArrayByteBufferCollector<T> implements Collector<T, JsonGeneratorCombiner<T>, Pair<byte[], Long>> {
	private final ObjectMapper objMapper;
	private final int maxBufSz;
	
	private final AtomicInteger combinerIds = new AtomicInteger();
	
	public JsonArrayByteBufferCollector(ObjectMapper objMapper, int maxBufSz) {
		this.objMapper = objMapper;
		this.maxBufSz = maxBufSz;
	}

	@Override
	public Supplier<JsonGeneratorCombiner<T>> supplier() {
		return () -> { 
			try {
				int combinerId = combinerIds.getAndIncrement();

				return new JsonGeneratorCombiner<>(combinerId, objMapper, new ByteArrayOutputStream(4096), maxBufSz);
			} catch (IOException ioEx) {
				throw new IllegalStateException("json array combiner creation failed: ", ioEx);
			}
		};
	}

	@Override
	public BiConsumer<JsonGeneratorCombiner<T>, T> accumulator() {
		return (c, o) -> {
			try {
				c.write(o);
			} catch (IOException ioEx) {
				throw new IllegalStateException("json array accumulation failed: ", ioEx);
			}
		};
	}


	@Override
	public BinaryOperator<JsonGeneratorCombiner<T>> combiner() {
		return (c1, c2) -> {
			try {
				return c1.merge(c2);
			} catch (IOException ioEx) {
				throw new IllegalStateException("json array merge failed: ", ioEx);
			}
		};
	}

	@Override
	public Function<JsonGeneratorCombiner<T>, Pair<byte[], Long>> finisher() {
		return (c) -> { 
			try {
				c.finish();
			} catch (IOException ioEx) {
				throw new IllegalStateException("json array combiner finish failed: ", ioEx);
			}
			
			return Pair.of(c.getBytes(), c.getCount());
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}
}
