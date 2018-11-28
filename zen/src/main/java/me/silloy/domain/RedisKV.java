package me.silloy.domain;

import lombok.Data;

@Data
public class RedisKV<K,V> {
	private K k;
	private V v;
	private Long ttl;

	public RedisKV(K key, V value) {
		this.k = key;
		this.v = value;
	}
	public RedisKV() {
	}
}