package org.logblock.entry;

import org.logblock.entry.blob.PaintingBlob;
import org.logblock.storage.DataStore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;

public abstract class BlobEntry extends AbstractEntry
{

    @SuppressWarnings("unchecked")
    private static final Class<? extends BlobEntry>[] mappings = new Class[256];
    private byte type;

    public BlobEntry(int id, byte type)
    {
        super(id);
        this.type = type;
    }

    public abstract void read(DataInput in) throws IOException;

    public abstract void write(DataOutput out) throws IOException;

    static
    {
        mappings[0] = PaintingBlob.class;
    }

    public static BlobEntry create(int id, byte type)
    {
        Class<? extends BlobEntry> mapping = mappings[type];

        BlobEntry ret = null;
        if (mapping != null)
        {
            try
            {
                ret = mapping.getDeclaredConstructor(int.class, byte.class).newInstance(id, type);
            } catch (Exception ex)
            {
                DataStore.getInstance().getLogger().log(Level.SEVERE, "Could not create blob with type " + type, ex);
            }
        }

        return ret;
    }
}
