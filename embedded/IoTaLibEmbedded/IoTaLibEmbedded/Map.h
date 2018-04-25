#pragma once
#include <utility>
namespace iota{

	// Simple constant size, dynamic allocation free array based
	// map implementation
	template<class K, class V>
	class Map
	{
		public:
			Map(int size);
			V put(K key, V value);
			V get(K key);
			V remove(K key);
			int contains(K key);
			int size();
			int freeSpace();
			int clear();
			
			//this method is very bad and I should really learn how to use iterators
			//**entries
			~Map();

		private:
			int maxSize;
			int currentSize;
			std::pair<K, V> *entries;

			

	};

	template<class K, class V>
	Map<K,V>::Map(int size)
	{
		maxSize = size;
		currentSize = 0;
		entries = new std::pair<K, V>[size];
		for (int i = 0; i < maxSize; i++) {
			entries[i].first = NULL;
		}
	}

	template<class K, class V>
	V Map<K,V>::put(K key, V value) {
		if (currentSize >= maxSize)
		{
			return NULL;
		}
		V val = NULL;
		for (int i = 0; i < maxSize; i++) {
			if (entries[i].first == NULL){
				entries[i].first = key;
				entries[i].second = value;
				currentSize++;
				return val;

			}
			else if (entries[i].first == key) {
				val = entries[i].second;
				entries[i].first = key;
				entries[i].second = value;
				return val;
			}
		}

		return val;
	}

	template<class K, class V>
	inline V Map<K, V>::get(K key)
	{
		for (int i = 0; i < maxSize; i++) {
			if (entries[i].first == key) {
				return entries[i].second;
			}
		}
		return NULL;
	}

	template<class K, class V>
	inline V Map<K, V>::remove(K key)
	{
		V val = NULL;
		for (int i = 0; i < maxSize; i++) {
			if (entries[i].first = key) {
				val = entries[i].first;
				entries[i].first = NULL;
				currentSize--;
			}
		}
		return val;
	}

	template<class K, class V>
	inline int Map<K, V>::contains(K key)
	{
		for (int i = 0; i < maxSize; i++) {
			if (entries[i].first == key) {
				return 1;
			}
		}
		return 0;
	}

	template<class K, class V>
	inline int Map<K, V>::size()
	{
		return currentSize;
	}

	template<class K, class V>
	inline int Map<K, V>::freeSpace()
	{
		return maxSize - currentSize;
	}

	template<class K, class V>
	inline int Map<K, V>::clear()
	{
		for (int i = 0; i < maxSize; i++) {
			entries[i].first = NULL;
		}
		return 0;
	}

	template<class K, class V>
	Map<K,V>::~Map()
	{
		delete entries;
	}


};