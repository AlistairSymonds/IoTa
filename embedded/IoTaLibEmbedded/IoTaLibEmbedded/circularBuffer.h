#pragma once
template <class T>
class CircularBuffer
{
public:
	CircularBuffer(int size);
	int add(T item);
	

	T read();
	int readMultiple(T* loc, int numItems);
	int available();

	~CircularBuffer();
private:
	int insertPos = 0;
	int readPos = 0;
	int fullSpots;
	int totalSize;
	T *buffer;
	
	int incPos(int pos);
	int decPos(int pos);
};


template<class T>
CircularBuffer<T>::CircularBuffer(int size)
{
	totalSize = size;
	fullSpots = 0;
	insertPos = 0;
	readPos = 0;
	buffer = new T[size];
}

template<class T>
int CircularBuffer<T>::add(T item)
{
	if (fullSpots < totalSize) {
		buffer[insertPos] = item;
		fullSpots++;
		insertPos = incPos(insertPos);
		return fullSpots;
	}
	else
	{
		buffer[insertPos] = item;
		insertPos = incPos(insertPos);
		return -1;
	}

}

template<class T>
T CircularBuffer<T>::read()
{
	T val = buffer[readPos];
	readPos = incPos(readPos);
	fullSpots--;

	return val;
}

template<class T>
int CircularBuffer<T>::readMultiple(T * loc, int numItems)
{
	return 0;
}

template<class T>
int CircularBuffer<T>::available()
{
	return fullSpots;
}





template<class T>
CircularBuffer<T>::~CircularBuffer()
{
	delete buffer;
}

template<class T>
int CircularBuffer<T>::incPos(int pos)
{
	pos++;
	if (pos == totalSize) {
		pos = 0;
	}
	return pos;
}

template<class T>
int CircularBuffer<T>::decPos(int pos)
{
	pos--;
	if (pos == -1) {
		pos = totalSize - 1;
	}
	return pos;
}

