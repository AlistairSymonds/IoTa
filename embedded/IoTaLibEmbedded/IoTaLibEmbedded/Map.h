#pragma once

namespace iota{

	// Simple constant size, dynamic allocation free array based
	// map implementation
	template<class K, class V>
	class Map
	{
		public:
			Map(int size);
			V put(K key, V value);
			~Map();

		private:
			std::pair<K, V> *entries;
	};

	template<class K, class V>
	Map<K,V>::Map(int size)
	{
		entries = new std::pair<K, V>[size];
	}

	template<class K, class V>
	V Map<K,V>::put(K key, V value) {
		
		return NULL;
	}

	template<class K, class V>
	Map<K,V>::~Map()
	{
		delete entries;
	}
};