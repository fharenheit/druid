package com.metamx.druid.partition;

import com.google.common.collect.Ordering;

import java.util.Comparator;

/**
 */
public class StringPartitionChunk<T> implements PartitionChunk<T>
{
  private static final Comparator<String> comparator = Ordering.<String>natural().nullsFirst();

  private final String start;
  private final String end;
  private final int chunkNumber;
  private final T object;

  public static <T> StringPartitionChunk<T> make(String start, String end, int chunkNumber, T obj)
  {
    return new StringPartitionChunk<T>(start, end, chunkNumber, obj);
  }


  public StringPartitionChunk(
      String start,
      String end,
      int chunkNumber,
      T object
  )
  {
    this.start = start;
    this.end = end;
    this.chunkNumber = chunkNumber;
    this.object = object;
  }

  @Override
  public T getObject()
  {
    return object;
  }

  @Override
  public boolean abuts(PartitionChunk<T> chunk)
  {
    if (chunk instanceof StringPartitionChunk) {
      StringPartitionChunk<T> stringChunk = (StringPartitionChunk<T>) chunk;

      return !stringChunk.isStart() && stringChunk.start.equals(end);
    }

    return false;
  }

  @Override
  public boolean isStart()
  {
    return start == null;
  }

  @Override

  public boolean isEnd()
  {
    return end == null;
  }

  @Override
  public int getChunkNumber()
  {
    return chunkNumber;
  }

  @Override
  public int compareTo(PartitionChunk<T> chunk)
  {
    if (chunk instanceof StringPartitionChunk) {
      StringPartitionChunk<T> stringChunk = (StringPartitionChunk<T>) chunk;

      int retVal = comparator.compare(start, stringChunk.start);
      if (retVal == 0) {
        retVal = comparator.compare(end, stringChunk.end);
      }

      return retVal;
    }
    throw new IllegalArgumentException("Cannot compare against something that is not a StringPartitionChunk.");
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return compareTo((StringPartitionChunk<T>) o) == 0;
  }

  @Override
  public int hashCode()
  {
    int result = start != null ? start.hashCode() : 0;
    result = 31 * result + (end != null ? end.hashCode() : 0);
    result = 31 * result + (object != null ? object.hashCode() : 0);
    return result;
  }
}