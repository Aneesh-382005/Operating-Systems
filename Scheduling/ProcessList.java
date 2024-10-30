public class ProcessList
{
    private Process[] data;
    private int size;

    public ProcessList()
    {
        data = new Process[5];
        size = 0;
    }

    public void add(Process process)
    {
        if(size >= data.length)
        {
            resize();
        }
        data[size++] = process;
    }

    public void resize()
    {
        Process [] newData = new Process [data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    public Process get(int index)
    {
        if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    public int size()
    {
        return size;
    }

    public void remove(int index)
    {
        if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        size--;
    }

    public void clear()
    {
        size = 0;
    }

    public Process[] toArray()
    {
        Process[] result = new Process[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }
}
