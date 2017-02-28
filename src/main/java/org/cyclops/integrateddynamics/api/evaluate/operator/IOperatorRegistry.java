package org.cyclops.integrateddynamics.api.evaluate.operator;

import org.cyclops.cyclopscore.init.IRegistry;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.item.IOperatorVariableFacade;
import org.cyclops.integrateddynamics.api.item.IVariableFacadeHandler;

import java.util.Collection;

/**
 * Registry for {@link IOperator}
 * @author rubensworks
 */
public interface IOperatorRegistry extends IRegistry, IVariableFacadeHandler<IOperatorVariableFacade> {

    /**
     * Register a new operator.
     * @param operator The operator.
     * @param <O> The operator type.
     * @return The registered operator.
     */
    public <O extends IOperator> O register(O operator);

    /**
     * @return All registered operators.
     */
    public Collection<IOperator> getOperators();

    /**
     * Get the operator with the given name.
     * @param operatorName The unique operator name.
     * @return The corresponding operator or null.
     */
    public IOperator getOperator(String operatorName);

    /**
     * Get the operators with the given input value types in that specific order.
     * @param valueTypes The input value types.
     * @return The corresponding operators.
     */
    public Collection<IOperator> getOperatorsWithInputTypes(IValueType... valueTypes);

    /**
     * Get the operators with the given output value type.
     * @param valueType The output value type.
     * @return The corresponding operators.
     */
    public Collection<IOperator> getOperatorsWithOutputType(IValueType valueType);

    /**
     * Register an operator serializer.
     * @param serializer The operator serializer.
     */
    public void registerSerializer(IOperatorSerializer serializer);

    /**
     * Serialize the given operator.
     * @param value The operator to serialize.
     * @return The serialized operator value.
     */
    public String serialize(IOperator value);

    /**
     * Deserialize the given operator value.
     * @param value The operator value to deserialize.
     * @return The deserialized operator.
     * @throws EvaluationException If an error occurs while deserializing.
     */
    public IOperator deserialize(String value) throws EvaluationException;

}
