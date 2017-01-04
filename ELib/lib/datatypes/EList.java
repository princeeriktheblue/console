package lib.datatypes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import lib.exceptions.InvalidDataException;

public class EList <E> implements Collection<E>, Iterable<E>, Comparable<EList<E>>, List<E>
{
	public EList()
	{}
	
	public EList(E[] array)
	{
		for(E data: array)
			add(data);
	}

	@Override
	public void add(int arg0, E arg1) 
	{
		Node temp = getNodeAt(arg0);
		if(temp != null)
		{
			Node toadd = new Node();
			toadd.data = arg1;
			toadd.tail = temp.tail;
			if(toadd.tail != null)
				toadd.tail.head = toadd;
			toadd.head = temp;
			if(toadd.head != null)
				toadd.head.tail = toadd;
			size++;
		}
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) 
	{
		checkBounds(arg0);
		int counter = arg0;
		Iterator<? extends E> it = arg1.iterator();
		while(it.hasNext())
			add(counter++, it.next());
		return arg1.size() > 0;
	}

	@Override
	public E get(int arg0) 
	{
		checkBounds(arg0);
		return getNodeAt(arg0).data;
	}

	@Override
	public int indexOf(Object arg0) 
	{
		int index = 0;
		Node temp = head;
		while(temp != null)
		{
			if(temp.data.equals(arg0))
				return index;
			else
			{
				temp = temp.head;
				index++;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object arg0) 
	{
		int index = size-1;
		Node temp = tail;
		while(temp != null)
		{
			if(temp.data.equals(arg0))
				return index;
			else
			{
				temp = temp.tail;
				index--;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() 
	{
		return new EListIterator(this);
	}

	@Override
	public ListIterator<E> listIterator(int arg0) 
	{
		checkBounds(arg0);
		return new EListIterator(this, arg0);
	}

	@Override
	public E remove(int arg0) 
	{
		checkBounds(arg0);
		Node temp = getNodeAt(arg0);
		if(temp != null)
		{
			E data = temp.data;
			removeNode(temp);
			return data;
		}
		return null;
	}

	@Override
	public E set(int arg0, E arg1) 
	{
		checkBounds(arg0);
		Node temp = getNodeAt(arg0);
		if(temp != null)
		{
			E data = temp.data;
			temp.data = arg1;
			return data;
		}
		return null;
	}

	@Override
	public List<E> subList(int arg0, int arg1) 
	{
		if(arg0 > arg1)
			throw new IndexOutOfBoundsException("" + arg0 + ">" + arg1);
		checkBounds(arg0);
		checkBounds(arg1);
		EList<E> list = new EList<>();
		Node temp = getNodeAt(arg0);
		int count = 0;
		int num = arg1 - arg0;
		while(temp != null && count < num)
		{
			list.add(temp.data);
			temp = temp.head;
			count++;
		}
		return list;
	}
	
	@Override
	public int compareTo(EList<E> o) 
	{
		return size - o.size;
	}

	@Override
	public boolean add(E e) 
	{
		Node toadd = new Node();
		toadd.data = e;
		if(size == 0 || head == null)
			head = toadd;
		else
		{
			tail.head = toadd;
			toadd.tail = tail;
		}
		tail = toadd;
		size++;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) 
	{
		Iterator<? extends E> it = c.iterator();
		while(it.hasNext())
			add(it.next());
		return true;
	}

	@Override
	public void clear()
	{
		while(size > 0 && head != null)
		{
			Node temp = head.head;
			head.data = null;
			head.head = null;
			head.tail = null;
			head = null;
			head = temp;
			size--;
		}
	}

	@Override
	public boolean contains(Object o)
	{
		Node temp = head;
		while(temp != null)
			if(temp.data.equals(o))
				return true;
			else
				temp = temp.head;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) 
	{
		Iterator<?> it = c.iterator();
		while(it.hasNext())
		{
			Object data = it.next();
			Node current = head;
			boolean found = false;
			while(current != null && !found)
				if(current.data.equals(data))
					found = true;
				else
					current = current.head;
			if(!found)
				return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() 
	{
		return size == 0;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new EIterator();
	}

	@Override
	public boolean remove(Object o)
	{
		Node current = head;
		while(current != null)
			if(current.data.equals(o))
			{
				removeNode(current);
				return true;
			}
			else
				current = current.head;
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean changed = false;
		Iterator<?> it = c.iterator();
		while(it.hasNext())
		{
			Object o = it.next();
			Node temp = head;
			while(temp != null)
			{
				if(temp.data.equals(o))
				{
					if(!changed)
						changed = true;
					Node toDelete = temp;
					temp = temp.head;
					removeNode(toDelete);
				}
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) 
	{
		Node temp = head;
		boolean changed = false;
		while(temp != null)
		{
			boolean contained = false;
			Iterator<?> it = c.iterator();
			while(it.hasNext() && !contained)
				if(it.next().equals(temp.data))
					contained = true;
			if(!contained)
			{
				Node toRemove = temp;
				temp = temp.head;
				removeNode(toRemove);
				if(!changed)
					changed = true;
			}
		}
		return changed;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public Object[] toArray() 
	{
		Node temp = head;
		Object[] arr = new Object[size];
		int counter = 0;
		while(counter < size)
		{
			arr[counter++] = temp.data;
			temp = temp.head;
		}
		return arr;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <E> E[] toArray(E[] a) 
	{
		Node temp = head;
		int counter = 0;
		int max = a.length > size ? size : a.length;
		while(counter < max)
		{
			a[counter++] = (E) temp.data;
			temp = temp.head;
		}
		return a;
	}
	
	public static EList<String> makeListFromString(String str) throws InvalidDataException
	{
		EList<String> toReturn = new EList<>();
		String temp = str;
		if(str.charAt(0) != '{' && str.charAt(str.length()-1) != '}')
			throw new InvalidDataException("The data is not valid");
		while(temp.length() > 0)
		{
			int index = temp.indexOf(',');
			String toAdd = temp.substring(0, index);
			toReturn.add(toAdd);
			temp = temp.substring(index+1);
		}
		return toReturn;
	}
	
	private void removeNode(Node n)
	{
		if(n == tail)
		{
			if(n == head)
			{
				head = null;
				tail = null;
			}
			else
			{
				tail = tail.tail;
				tail.head = null;
			}
		}
		else
		{
			if(n == head)
			{
				head = head.head;
				head.tail = null;
			}
			else
			{
				n.head.tail = n.tail;
				n.tail.head = n.head;
			}
		}
		size--;
		n = null;
	}
	
	private Node getNodeAt(int arg0)
	{
		boolean flip = arg0 > size/2;
		Node temp = flip ? tail : head;
		int index = flip ? size-1 : 0;
		if(flip)
		{
			while(index > arg0 && temp != null)
			{
				temp = temp.tail;
				index--;
			}
		}
		else
		{
			while(index < arg0 && temp != null)
			{
				temp = temp.head;
				index++;
			}
		}
		return temp;
	}
	
	private void checkBounds(int arg0) throws IndexOutOfBoundsException
	{
		if(arg0 < 0 || arg0 >= size)
			throw new IndexOutOfBoundsException("" + arg0);
	}
	
	private int size;
	private Node head;
	private Node tail;
	
	private final class Node
	{
		private Node head;
		private Node tail;
		private E data;
	}
	
	private final class EIterator implements Iterator<E>
	{
		public EIterator()
		{
			current = head;
		}

		@Override
		public boolean hasNext() 
		{
			return current != null;
		}

		@Override
		public E next() 
		{
			E data = current.data;
			current = current.head;
			return data;
		}
		
		private Node current;
	}
	
	private final class EListIterator implements ListIterator<E>
	{
		public EListIterator(EList<E> parent)
		{
			current = head;
			index = 0;
			this.parent = parent;
		}
		
		public EListIterator(EList<E> parent, int arg0)
		{
			current = getNodeAt(arg0);
			index = arg0;
			this.parent = parent;
		}
		
		@Override
		public void add(E arg0) 
		{
			parent.add(index, arg0);
			index++;
		}

		@Override
		public boolean hasNext() 
		{
			return current != null;
		}

		@Override
		public boolean hasPrevious() 
		{
			return current != null;
		}

		@Override
		public E next() 
		{
			return move(true);
		}

		@Override
		public int nextIndex() 
		{
			return index+1;
		}

		@Override
		public E previous() 
		{
			return move(false);
		}

		@Override
		public int previousIndex() 
		{
			return index-1;
		}

		@Override
		public void remove() 
		{
			if(called || !moved)
				throw new IllegalStateException();
			if(current.tail != null)
				current = current.tail;
			removeNode(current);
			called = true;
		}

		@Override
		public void set(E arg0) 
		{
			current.data = arg0;
		}
		
		private E move(boolean forward)
		{
			moved = true;
			called = false;
			current = forward ? current.head: current.tail;
			index = forward ? index+1 : index-1;
			return current.data;
		}
		
		private Node current;
		private int index;
		private boolean moved;
		private boolean called;
		private final EList<E> parent;
	}
}