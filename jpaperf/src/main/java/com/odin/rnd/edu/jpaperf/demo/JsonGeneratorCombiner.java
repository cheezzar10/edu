package com.odin.rnd.edu.jpaperf.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonGeneratorCombiner<T> {
	private final int id;
	
	private final ObjectMapper objMapper;

	private final ByteArrayOutputStream buf;
	private final int maxBufSz;

	private final JsonGenerator jsonGen;
	
	private long count = 0;
	
	private static final AtomicInteger ids = new AtomicInteger();

	public JsonGeneratorCombiner(ObjectMapper objMapper, ByteArrayOutputStream buf, int maxBufSz) throws IOException {
		this(objMapper, buf, maxBufSz, 0L);
	}

	public JsonGeneratorCombiner(ObjectMapper objMapper, ByteArrayOutputStream buf, int maxBufSz, long count) throws IOException {
		this.objMapper = objMapper;
		
		this.buf = buf;
		this.maxBufSz = maxBufSz;
		
		this.jsonGen = objMapper.getFactory().createGenerator(buf);
		
		this.count = count;
		
		id = ids.getAndIncrement();
	}

	public void write(T o) throws IOException {
		// first combiner first write identification
		if (id == 0 && count == 0) {
			jsonGen.writeStartArray();
		}

		jsonGen.writeObject(o);
		
		checkBufSizeLimit();
		
		count++;
	}

	private void checkBufSizeLimit() {
		if (buf.size() >= maxBufSz) {
			throw new IllegalStateException("buffer size limit " + maxBufSz + " exceeded");
		}
	}

	public JsonGeneratorCombiner<T> merge(JsonGeneratorCombiner<T> other) throws IOException {
		other.buf.writeTo(buf);
		
		return new JsonGeneratorCombiner<>(objMapper, buf, maxBufSz, count + other.count);
	}

	public void finish() throws IOException {
		jsonGen.writeEndArray();

		jsonGen.flush();
	}

	public byte[] getBytes() {
		return buf.toByteArray();
	}

	public long getCount() {
		return count;
	}
}
