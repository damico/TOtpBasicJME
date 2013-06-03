package javame.helpers;

import javame.helpers.Collection;
import javame.helpers.Iterator;
import javame.helpers.Set;

public abstract class AbstractSet
    extends AbstractCollection
    implements Set
{
    protected AbstractSet()
    {
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null)
        {
            return false;
        }
        if (!(o instanceof Set))
        {
            return false;
        }
        if (((Set)o).size() != size())
        {
            return false;
        }
        return containsAll((Collection)o);
    }

    public int hashCode()
    {
        int hashCode = 0;
        Iterator it = iterator();
        while (it.hasNext())
        {
            Object o = it.next();
            if (o != null)
            {
                hashCode += o.hashCode();
            }
        }
        return hashCode;
    }
}
