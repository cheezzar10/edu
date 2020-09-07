package com.odin.rnd.edu.jpaperf.demo;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odin.rnd.edu.jpaperf.model.Account;

public class StreamingJsonDemo {
	public static void main(String... args) throws Exception {
		List<Account> accounts = new ArrayList<>();
		
		Account provider = new Account("Acme Corp");
		provider.setId(1);

		accounts.add(provider);
		
		Account reseller = new Account("Large Reseller");
		reseller.setId(2);
		reseller.setOwner(provider);
		
		accounts.add(reseller);

		ObjectMapper objMapper = new ObjectMapper();
		JsonFactory jsonFactory = objMapper.getFactory();
		try (FileOutputStream fs = new FileOutputStream("accounts-1.json");
				JsonGenerator jsonGen = jsonFactory.createGenerator(fs, JsonEncoding.UTF8)) {
			
			jsonGen.writeStartArray();

			accounts.stream().forEach(a -> { 
				try {
					jsonGen.writeObject(a);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			
			jsonGen.writeEndArray();
		}
		
		try (FileOutputStream fs = new FileOutputStream("accounts-2.json");
				JsonGenerator jsonGen = jsonFactory.createGenerator(fs)) {
			
			Pair<byte[], Long> result = accounts
					.stream()
					.collect(new JsonArrayByteBufferCollector<>(objMapper, 256));
			
			fs.write(result.getLeft());
			
			System.out.printf("total rows = %d%n", result.getRight());
		}
	}
}
