package org.cyclops.integrateddynamics.core.evaluate.variable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxyFactoryTypeRegistry;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;

import java.util.concurrent.TimeUnit;

/**
 * A list that is built lazily from a start value and an operator.
 * @param <T> The value type type.
 * @param <V> The value type.
 */
public class ValueTypeListProxyLazyBuilt<T extends IValueType<V>, V extends IValue> extends ValueTypeListProxyBase<T, V> {

    private Cache<Integer, V> cache_values = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).maximumSize(100).build();

    private final V value;
    private final IOperator operator;

    public ValueTypeListProxyLazyBuilt(V value, IOperator operator) {
        super(ValueTypeListProxyFactories.LAZY_BUILT.getName(), (T) value.getType());
        this.value = value;
        this.operator = operator;
    }

    @Override
    public int getLength() throws EvaluationException {
        return Integer.MAX_VALUE;
    }

    @Override
    public V get(int index) throws EvaluationException {
        if (index == 0) {
            return value;
        }
        V current = cache_values.getIfPresent(index);
        if (current != null) {
            return current;
        }
        V previous = get(index - 1);
        current = (V) operator.evaluate(new IVariable[]{new Variable(previous.getType(), previous)});
        cache_values.put(index, current);
        return current;
    }

    @Override
    public boolean isInfinite() {
        return true;
    }

    public static class Factory implements IValueTypeListProxyFactoryTypeRegistry.IProxyFactory<IValueType<IValue>, IValue, ValueTypeListProxyLazyBuilt<IValueType<IValue>, IValue>> {

        @Override
        public String getName() {
            return "lazybuilt";
        }

        @Override
        public String serialize(ValueTypeListProxyLazyBuilt<IValueType<IValue>, IValue> values) throws IValueTypeListProxyFactoryTypeRegistry.SerializationException {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("valueType", values.value.getType().getUnlocalizedName());
            tag.setString("value", values.value.getType().serialize(values.value));
            tag.setString("operator", Operators.REGISTRY.serialize(values.operator));
            return tag.toString();
        }

        @Override
        public ValueTypeListProxyLazyBuilt<IValueType<IValue>, IValue> deserialize(String data) throws IValueTypeListProxyFactoryTypeRegistry.SerializationException {
            try {
                NBTTagCompound tag = JsonToNBT.getTagFromJson(data);
                IValueType valueType = ValueTypes.REGISTRY.getValueType(tag.getString("valueType"));
                IValue value = valueType.deserialize(tag.getString("value"));
                IOperator operator = Operators.REGISTRY.deserialize(tag.getString("operator"));
                return new ValueTypeListProxyLazyBuilt<>(value, operator);
            } catch (NBTException | EvaluationException e) {
                e.printStackTrace();
                throw new IValueTypeListProxyFactoryTypeRegistry.SerializationException(e.getMessage());
            }
        }
    }
}
