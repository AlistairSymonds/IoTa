#pragma once
#ifndef _FIXEDSET_H
#define _FIXEDSET_H

//this entire class should be replaced by a hashmap
template <class T>
class fixedSet
{
public:
	fixedSet(int size);
	int add(T item);
	int contains(T item);
	int remove(T item);
	int size();
	~fixedSet();
private:
	int freeSpace;
	int totalSize;
	T *heap;
	uint8_t* occupied;

};


template<class T>
fixedSet<T>::fixedSet(int size)
{
	freeSpace = size;
	totalSize = size;
	heap = new T[size];
	occupied = new uint8_t[size];
	for (int i = 0; i < size; i++) {
		occupied[i] = 0;
	}
}

template<class T>
int fixedSet<T>::add(T item)
{	
	if(freeSpace > 0) {
		for (int i = 0; i < totalSize; i++) {
			if (occupied[i] == 0) {
				heap[i] = item;
				occupied[i] = 1;
				return 1;
			}
		}
	}
	return 0;
}

template<class T>
int fixedSet<T>::contains(T item)
{
	for (int i = 0; i < totalSize; i++) {
		if (heap[i] == item && occupied[i] == 1) {
			return 1;
		}
	}
	return 0;
}

template<class T>
int fixedSet<T>::remove(T item)
{
	int removed = 0;
	for (int i = 0; i < totalSize; i++) {
		if (heap[i] == item) {
			removed = 1;
			occupied[i] = 0;
			freeSpace++;
			return 1;
		}
	}
	return -1;
}

template<class T>
int fixedSet<T>::size()
{
	return totalSize - freeSpace;
}

template<class T>
fixedSet<T>::~fixedSet()
{
	delete occupied;
	delete heap;
}
#endif
