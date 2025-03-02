/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.siddhi.core.util.parser;

import io.siddhi.core.config.SiddhiQueryContext;
import io.siddhi.core.event.MetaComplexEvent;
import io.siddhi.core.event.state.MetaStateEvent;
import io.siddhi.core.event.state.StateEvent;
import io.siddhi.core.event.stream.MetaStreamEvent;
import io.siddhi.core.exception.ExtensionNotFoundException;
import io.siddhi.core.exception.OperationNotSupportedException;
import io.siddhi.core.exception.SiddhiAppCreationException;
import io.siddhi.core.executor.ConstantExpressionExecutor;
import io.siddhi.core.executor.EventVariableFunctionExecutor;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.MultiValueVariableFunctionExecutor;
import io.siddhi.core.executor.VariableExpressionExecutor;
import io.siddhi.core.executor.condition.AndConditionExpressionExecutor;
import io.siddhi.core.executor.condition.BoolConditionExpressionExecutor;
import io.siddhi.core.executor.condition.ConditionExpressionExecutor;
import io.siddhi.core.executor.condition.InConditionExpressionExecutor;
import io.siddhi.core.executor.condition.IsNullConditionExpressionExecutor;
import io.siddhi.core.executor.condition.IsNullStreamConditionExpressionExecutor;
import io.siddhi.core.executor.condition.NotConditionExpressionExecutor;
import io.siddhi.core.executor.condition.OrConditionExpressionExecutor;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorBoolBool;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.equal.EqualCompareConditionExpressionExecutorStringString;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.greaterthanequal.GreaterThanEqualCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.lessthanequal.LessThanEqualCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorBoolBool;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorDoubleDouble;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorDoubleFloat;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorDoubleInt;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorDoubleLong;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorFloatDouble;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorFloatFloat;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorFloatInt;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorFloatLong;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorIntDouble;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorIntFloat;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorIntInt;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorIntLong;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorLongDouble;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorLongFloat;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorLongInt;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorLongLong;
import io.siddhi.core.executor.condition.compare.notequal.NotEqualCompareConditionExpressionExecutorStringString;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.executor.function.ScriptFunctionExecutor;
import io.siddhi.core.executor.math.add.AddExpressionExecutorDouble;
import io.siddhi.core.executor.math.add.AddExpressionExecutorFloat;
import io.siddhi.core.executor.math.add.AddExpressionExecutorInt;
import io.siddhi.core.executor.math.add.AddExpressionExecutorLong;
import io.siddhi.core.executor.math.divide.DivideExpressionExecutorDouble;
import io.siddhi.core.executor.math.divide.DivideExpressionExecutorFloat;
import io.siddhi.core.executor.math.divide.DivideExpressionExecutorInt;
import io.siddhi.core.executor.math.divide.DivideExpressionExecutorLong;
import io.siddhi.core.executor.math.mod.ModExpressionExecutorDouble;
import io.siddhi.core.executor.math.mod.ModExpressionExecutorFloat;
import io.siddhi.core.executor.math.mod.ModExpressionExecutorInt;
import io.siddhi.core.executor.math.mod.ModExpressionExecutorLong;
import io.siddhi.core.executor.math.multiply.MultiplyExpressionExecutorDouble;
import io.siddhi.core.executor.math.multiply.MultiplyExpressionExecutorFloat;
import io.siddhi.core.executor.math.multiply.MultiplyExpressionExecutorInt;
import io.siddhi.core.executor.math.multiply.MultiplyExpressionExecutorLong;
import io.siddhi.core.executor.math.subtract.SubtractExpressionExecutorDouble;
import io.siddhi.core.executor.math.subtract.SubtractExpressionExecutorFloat;
import io.siddhi.core.executor.math.subtract.SubtractExpressionExecutorInt;
import io.siddhi.core.executor.math.subtract.SubtractExpressionExecutorLong;
import io.siddhi.core.query.processor.ProcessingMode;
import io.siddhi.core.query.selector.attribute.aggregator.AttributeAggregatorExecutor;
import io.siddhi.core.table.Table;
import io.siddhi.core.util.ExceptionUtil;
import io.siddhi.core.util.SiddhiClassLoader;
import io.siddhi.core.util.SiddhiConstants;
import io.siddhi.core.util.collection.operator.CompiledCondition;
import io.siddhi.core.util.collection.operator.MatchingMetaInfoHolder;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.extension.holder.AttributeAggregatorExtensionHolder;
import io.siddhi.core.util.extension.holder.FunctionExecutorExtensionHolder;
import io.siddhi.query.api.definition.AbstractDefinition;
import io.siddhi.query.api.definition.Attribute;
import io.siddhi.query.api.exception.AttributeNotExistException;
import io.siddhi.query.api.exception.DuplicateAttributeException;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import io.siddhi.query.api.expression.AttributeFunction;
import io.siddhi.query.api.expression.Expression;
import io.siddhi.query.api.expression.Variable;
import io.siddhi.query.api.expression.condition.And;
import io.siddhi.query.api.expression.condition.Compare;
import io.siddhi.query.api.expression.condition.In;
import io.siddhi.query.api.expression.condition.IsNull;
import io.siddhi.query.api.expression.condition.Not;
import io.siddhi.query.api.expression.condition.Or;
import io.siddhi.query.api.expression.constant.BoolConstant;
import io.siddhi.query.api.expression.constant.Constant;
import io.siddhi.query.api.expression.constant.DoubleConstant;
import io.siddhi.query.api.expression.constant.FloatConstant;
import io.siddhi.query.api.expression.constant.IntConstant;
import io.siddhi.query.api.expression.constant.LongConstant;
import io.siddhi.query.api.expression.constant.StringConstant;
import io.siddhi.query.api.expression.math.Add;
import io.siddhi.query.api.expression.math.Divide;
import io.siddhi.query.api.expression.math.Mod;
import io.siddhi.query.api.expression.math.Multiply;
import io.siddhi.query.api.expression.math.Subtract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to parse Expressions and create Expression executors.
 */
public class ExpressionParser {

    /**
     * Parse the given expression and create the appropriate Executor by recursively traversing the expression
     *
     * @param expression                 Expression to be parsed
     * @param metaEvent                  Meta Event
     * @param currentState               Current state number
     * @param tableMap                   Event Table Map
     * @param executorList               List to hold VariableExpressionExecutors to update after query parsing
     * @param groupBy                    is for groupBy expression
     * @param defaultStreamEventIndex    Default StreamEvent Index
     * @param processingMode             processing mode of the query
     * @param outputExpectsExpiredEvents is expired events sent as output
     * @param siddhiQueryContext         current siddhi query context
     * @return ExpressionExecutor
     */
    public static ExpressionExecutor parseExpression(Expression expression, MetaComplexEvent metaEvent,
                                                     int currentState, Map<String, Table> tableMap,
                                                     List<VariableExpressionExecutor> executorList,
                                                     boolean groupBy,
                                                     int defaultStreamEventIndex,
                                                     ProcessingMode processingMode,
                                                     boolean outputExpectsExpiredEvents, SiddhiQueryContext siddhiQueryContext) {
        try {
            if (expression instanceof And) {
                return new AndConditionExpressionExecutor(
                        parseExpression(((And) expression).getLeftExpression(), metaEvent, currentState, tableMap,
                                executorList, groupBy, defaultStreamEventIndex,
                                processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                        parseExpression(((And) expression).getRightExpression(), metaEvent, currentState, tableMap,
                                executorList, groupBy, defaultStreamEventIndex,
                                processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
            } else if (expression instanceof Or) {
                return new OrConditionExpressionExecutor(
                        parseExpression(((Or) expression).getLeftExpression(), metaEvent, currentState, tableMap,
                                executorList, groupBy, defaultStreamEventIndex,
                                processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                        parseExpression(((Or) expression).getRightExpression(), metaEvent, currentState, tableMap,
                                executorList, groupBy, defaultStreamEventIndex,
                                processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
            } else if (expression instanceof Not) {
                return new NotConditionExpressionExecutor(
                        parseExpression(((Not) expression).getExpression(), metaEvent, currentState, tableMap,
                                executorList, groupBy, defaultStreamEventIndex,
                                processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
            } else if (expression instanceof Compare) {
                if (((Compare) expression).getOperator() == Compare.Operator.EQUAL) {
                    Expression leftExpression = ((Compare) expression).getLeftExpression();
                    Expression rightExpression = ((Compare) expression).getRightExpression();
                    ExpressionExecutor leftExpressionExecutor = parseExpression(
                            leftExpression, metaEvent, currentState,
                            tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                    ExpressionExecutor rightExpressionExecutor = parseExpression(
                            rightExpression, metaEvent, currentState,
                            tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                    return parseEqualCompare(leftExpressionExecutor, rightExpressionExecutor);
                } else if (((Compare) expression).getOperator() == Compare.Operator.NOT_EQUAL) {
                    return parseNotEqualCompare(
                            parseExpression(((Compare) expression).getLeftExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                            parseExpression(((Compare) expression).getRightExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
                } else if (((Compare) expression).getOperator() == Compare.Operator.GREATER_THAN) {
                    return parseGreaterThanCompare(
                            parseExpression(((Compare) expression).getLeftExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                            parseExpression(((Compare) expression).getRightExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
                } else if (((Compare) expression).getOperator() == Compare.Operator.GREATER_THAN_EQUAL) {
                    return parseGreaterThanEqualCompare(
                            parseExpression(((Compare) expression).getLeftExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                            parseExpression(((Compare) expression).getRightExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
                } else if (((Compare) expression).getOperator() == Compare.Operator.LESS_THAN) {
                    return parseLessThanCompare(
                            parseExpression(((Compare) expression).getLeftExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                            parseExpression(((Compare) expression).getRightExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
                } else if (((Compare) expression).getOperator() == Compare.Operator.LESS_THAN_EQUAL) {
                    return parseLessThanEqualCompare(
                            parseExpression(((Compare) expression).getLeftExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext),
                            parseExpression(((Compare) expression).getRightExpression(), metaEvent, currentState,
                                    tableMap, executorList, groupBy, defaultStreamEventIndex,
                                    processingMode, outputExpectsExpiredEvents, siddhiQueryContext));
                }

            } else if (expression instanceof Constant) {
                if (expression instanceof BoolConstant) {
                    return new ConstantExpressionExecutor(((BoolConstant) expression).getValue(), Attribute.Type.BOOL);
                } else if (expression instanceof StringConstant) {
                    return new ConstantExpressionExecutor(((StringConstant) expression).getValue(), Attribute.Type.STRING);
                } else if (expression instanceof IntConstant) {
                    return new ConstantExpressionExecutor(((IntConstant) expression).getValue(), Attribute.Type.INT);
                } else if (expression instanceof LongConstant) {
                    return new ConstantExpressionExecutor(((LongConstant) expression).getValue(), Attribute.Type.LONG);
                } else if (expression instanceof FloatConstant) {
                    return new ConstantExpressionExecutor(((FloatConstant) expression).getValue(), Attribute.Type.FLOAT);
                } else if (expression instanceof DoubleConstant) {
                    return new ConstantExpressionExecutor(((DoubleConstant) expression).getValue(), Attribute.Type.DOUBLE);
                }

            } else if (expression instanceof Variable) {
                return parseVariable((Variable) expression, metaEvent, currentState, executorList,
                        defaultStreamEventIndex, siddhiQueryContext);

            } else if (expression instanceof Multiply) {
                ExpressionExecutor left = parseExpression(((Multiply) expression).getLeftValue(), metaEvent,
                        currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                ExpressionExecutor right = parseExpression(((Multiply) expression).getRightValue(), metaEvent,
                        currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                Attribute.Type type = parseArithmeticOperationResultType(left, right);
                switch (type) {
                    case INT:
                        return new MultiplyExpressionExecutorInt(left, right);
                    case LONG:
                        return new MultiplyExpressionExecutorLong(left, right);
                    case FLOAT:
                        return new MultiplyExpressionExecutorFloat(left, right);
                    case DOUBLE:
                        return new MultiplyExpressionExecutorDouble(left, right);
                    default: // Will not happen. Handled in parseArithmeticOperationResultType()
                }
            } else if (expression instanceof Add) {
                ExpressionExecutor left = parseExpression(((Add) expression).getLeftValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                ExpressionExecutor right = parseExpression(((Add) expression).getRightValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                Attribute.Type type = parseArithmeticOperationResultType(left, right);
                switch (type) {
                    case INT:
                        return new AddExpressionExecutorInt(left, right);
                    case LONG:
                        return new AddExpressionExecutorLong(left, right);
                    case FLOAT:
                        return new AddExpressionExecutorFloat(left, right);
                    case DOUBLE:
                        return new AddExpressionExecutorDouble(left, right);
                    default: // Will not happen. Handled in parseArithmeticOperationResultType()
                }
            } else if (expression instanceof Subtract) {
                ExpressionExecutor left = parseExpression(((Subtract) expression).getLeftValue(), metaEvent,
                        currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                ExpressionExecutor right = parseExpression(((Subtract) expression).getRightValue(), metaEvent,
                        currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                Attribute.Type type = parseArithmeticOperationResultType(left, right);
                switch (type) {
                    case INT:
                        return new SubtractExpressionExecutorInt(left, right);
                    case LONG:
                        return new SubtractExpressionExecutorLong(left, right);
                    case FLOAT:
                        return new SubtractExpressionExecutorFloat(left, right);
                    case DOUBLE:
                        return new SubtractExpressionExecutorDouble(left, right);
                    default: // Will not happen. Handled in parseArithmeticOperationResultType()
                }
            } else if (expression instanceof Mod) {
                ExpressionExecutor left = parseExpression(((Mod) expression).getLeftValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                ExpressionExecutor right = parseExpression(((Mod) expression).getRightValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                Attribute.Type type = parseArithmeticOperationResultType(left, right);
                switch (type) {
                    case INT:
                        return new ModExpressionExecutorInt(left, right);
                    case LONG:
                        return new ModExpressionExecutorLong(left, right);
                    case FLOAT:
                        return new ModExpressionExecutorFloat(left, right);
                    case DOUBLE:
                        return new ModExpressionExecutorDouble(left, right);
                    default: // Will not happen. Handled in parseArithmeticOperationResultType()
                }
            } else if (expression instanceof Divide) {
                ExpressionExecutor left = parseExpression(((Divide) expression).getLeftValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                ExpressionExecutor right = parseExpression(((Divide) expression).getRightValue(), metaEvent, currentState,
                        tableMap, executorList, groupBy, defaultStreamEventIndex,
                        processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                Attribute.Type type = parseArithmeticOperationResultType(left, right);
                switch (type) {
                    case INT:
                        return new DivideExpressionExecutorInt(left, right);
                    case LONG:
                        return new DivideExpressionExecutorLong(left, right);
                    case FLOAT:
                        return new DivideExpressionExecutorFloat(left, right);
                    case DOUBLE:
                        return new DivideExpressionExecutorDouble(left, right);
                    default: // Will not happen. Handled in parseArithmeticOperationResultType()
                }

            } else if (expression instanceof AttributeFunction) {
                // extensions
                Object executor;
                try {
                    if ((siddhiQueryContext.getSiddhiAppContext().isFunctionExist(
                            ((AttributeFunction) expression).getName()))
                            && (((AttributeFunction) expression).getNamespace()).isEmpty()) {
                        executor = new ScriptFunctionExecutor(((AttributeFunction) expression).getName());
                    } else {
                        executor = SiddhiClassLoader.loadExtensionImplementation((AttributeFunction) expression,
                                FunctionExecutorExtensionHolder.getInstance(siddhiQueryContext.getSiddhiAppContext()));
                    }
                } catch (SiddhiAppCreationException ex) {
                    try {
                        executor = SiddhiClassLoader.loadExtensionImplementation((AttributeFunction) expression,
                                AttributeAggregatorExtensionHolder.getInstance(siddhiQueryContext.getSiddhiAppContext()));
                    } catch (SiddhiAppCreationException e) {
                        throw new ExtensionNotFoundException("'" + ((AttributeFunction) expression).getName() + "' is"
                                + " neither a function extension nor an aggregated attribute extension",
                                expression.getQueryContextStartIndex(), expression.getQueryContextEndIndex());
                    }
                }
                ConfigReader configReader = siddhiQueryContext.getSiddhiContext().getConfigManager().generateConfigReader(
                        ((AttributeFunction) expression).getNamespace(), ((AttributeFunction) expression).getName());
                if (executor instanceof FunctionExecutor) {
                    FunctionExecutor expressionExecutor = (FunctionExecutor) executor;
                    Expression[] innerExpressions = ((AttributeFunction) expression).getParameters();
                    ExpressionExecutor[] innerExpressionExecutors = parseInnerExpression(innerExpressions, metaEvent,
                            currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);

                    expressionExecutor.initExecutor(innerExpressionExecutors,
                            processingMode, configReader, groupBy, siddhiQueryContext);
                    if (expressionExecutor.getReturnType() == Attribute.Type.BOOL) {
                        return new BoolConditionExpressionExecutor(expressionExecutor);
                    }
                    return expressionExecutor;
                } else {
                    AttributeAggregatorExecutor attributeAggregatorExecutor = (AttributeAggregatorExecutor) executor;
                    Expression[] innerExpressions = ((AttributeFunction) expression).getParameters();
                    ExpressionExecutor[] innerExpressionExecutors = parseInnerExpression(innerExpressions, metaEvent,
                            currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                    attributeAggregatorExecutor.initAggregator(innerExpressionExecutors, processingMode,
                            outputExpectsExpiredEvents, configReader, groupBy, siddhiQueryContext);
//                    AbstractAggregationAttributeExecutor aggregationAttributeProcessor;
//                    if (groupBy) {
//                        aggregationAttributeProcessor = new GroupByAggregationAttributeExecutor(attributeAggregatorExecutor,
//                                innerExpressionExecutors, configReader, siddhiQueryContext);
//                    } else {
//                        aggregationAttributeProcessor = new AggregationAttributeExecutor(attributeAggregatorExecutor,
//                                innerExpressionExecutors, siddhiQueryContext);
//                    }
                    SelectorParser.getContainsAggregatorThreadLocal().set("true");
                    return attributeAggregatorExecutor;
                }
            } else if (expression instanceof In) {

                Table table = tableMap.get(((In) expression).getSourceId());
                MatchingMetaInfoHolder matchingMetaInfoHolder = MatcherParser.constructMatchingMetaStateHolder(
                        metaEvent, defaultStreamEventIndex, table.getTableDefinition(), defaultStreamEventIndex);
                CompiledCondition compiledCondition = table.compileCondition(((In) expression).getExpression(),
                        matchingMetaInfoHolder, executorList, tableMap, siddhiQueryContext);
                return new InConditionExpressionExecutor(table, compiledCondition,
                        matchingMetaInfoHolder.getMetaStateEvent().getMetaStreamEvents().length,
                        metaEvent instanceof StateEvent, 0);

            } else if (expression instanceof IsNull) {

                IsNull isNull = (IsNull) expression;

                if (isNull.getExpression() != null) {
                    ExpressionExecutor innerExpressionExecutor = parseExpression(isNull.getExpression(), metaEvent,
                            currentState, tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                    return new IsNullConditionExpressionExecutor(innerExpressionExecutor);
                } else {
                    String streamId = isNull.getStreamId();
                    Integer streamIndex = isNull.getStreamIndex();

                    if (metaEvent instanceof MetaStateEvent) {
                        int[] eventPosition = new int[2];
                        if (streamIndex != null) {
                            if (streamIndex <= SiddhiConstants.LAST) {
                                eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = streamIndex + 1;
                            } else {
                                eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = streamIndex;
                            }
                        } else {
                            eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = defaultStreamEventIndex;
                        }
                        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = SiddhiConstants.UNKNOWN_STATE;

                        MetaStateEvent metaStateEvent = (MetaStateEvent) metaEvent;
                        if (streamId == null) {
                            throw new SiddhiAppCreationException("IsNull does not support streamId being null",
                                    expression.getQueryContextStartIndex(), expression.getQueryContextEndIndex());
                        } else {
                            MetaStreamEvent[] metaStreamEvents = metaStateEvent.getMetaStreamEvents();
                            for (int i = 0, metaStreamEventsLength = metaStreamEvents.length; i < metaStreamEventsLength; i++) {
                                MetaStreamEvent metaStreamEvent = metaStreamEvents[i];
                                AbstractDefinition definition = metaStreamEvent.getLastInputDefinition();
                                if (metaStreamEvent.getInputReferenceId() == null) {
                                    if (definition.getId().equals(streamId)) {
                                        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                                        break;
                                    }
                                } else {
                                    if (metaStreamEvent.getInputReferenceId().equals(streamId)) {
                                        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                                        if (currentState > -1
                                                && metaStreamEvents[currentState].getInputReferenceId() != null
                                                && streamIndex != null && streamIndex <= SiddhiConstants.LAST) {
                                            if (streamId.equals(metaStreamEvents[currentState].getInputReferenceId())) {
                                                eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = streamIndex;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        return new IsNullStreamConditionExpressionExecutor(eventPosition);
                    } else {
                        return new IsNullStreamConditionExpressionExecutor(null);
                    }

                }
            }
            throw new UnsupportedOperationException(expression.toString() + " not supported!");
        } catch (Throwable t) {
            ExceptionUtil.populateQueryContext(t, expression, siddhiQueryContext.getSiddhiAppContext(),
                    siddhiQueryContext);
            throw t;
        }
    }

    /**
     * Create greater than Compare Condition Expression Executor which evaluates whether value of leftExpressionExecutor
     * is greater than value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseGreaterThanCompare(ExpressionExecutor leftExpressionExecutor,
                                                                       ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                throw new OperationNotSupportedException("string cannot used in greater than comparisons");
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new GreaterThanCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new GreaterThanCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new GreaterThanCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new GreaterThanCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                throw new OperationNotSupportedException("bool cannot used in greater than comparisons");
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " greater than comparisons");
        }
    }

    /**
     * Create greater than or equal Compare Condition Expression Executor which evaluates whether value of
     * leftExpressionExecutor
     * is greater than or equal to value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseGreaterThanEqualCompare(ExpressionExecutor leftExpressionExecutor,
                                                                            ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                throw new OperationNotSupportedException("string cannot used in greater than equal comparisons");
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanEqualCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanEqualCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanEqualCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanEqualCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanEqualCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanEqualCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new GreaterThanEqualCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new GreaterThanEqualCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new GreaterThanEqualCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                throw new OperationNotSupportedException("bool cannot used in greater than equal comparisons");
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " greater than comparisons");
        }
    }

    /**
     * Create less than Compare Condition Expression Executor which evaluates whether value of leftExpressionExecutor
     * is less than value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseLessThanCompare(ExpressionExecutor leftExpressionExecutor,
                                                                    ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                throw new OperationNotSupportedException("string cannot used in less than comparisons");
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new LessThanCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new LessThanCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new LessThanCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new LessThanCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                throw new OperationNotSupportedException("bool cannot used in less than comparisons");
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " less than comparisons");
        }
    }

    /**
     * Create less than or equal Compare Condition Expression Executor which evaluates whether value of
     * leftExpressionExecutor
     * is less than or equal to value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseLessThanEqualCompare(ExpressionExecutor leftExpressionExecutor,
                                                                         ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                throw new OperationNotSupportedException("string cannot used in less than equal comparisons");
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new LessThanEqualCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanEqualCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanEqualCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanEqualCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new LessThanEqualCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanEqualCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanEqualCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanEqualCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new LessThanEqualCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanEqualCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanEqualCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanEqualCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new LessThanEqualCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new LessThanEqualCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new LessThanEqualCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new LessThanEqualCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                throw new OperationNotSupportedException("bool cannot used in less than equal comparisons");
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " less than comparisons");
        }
    }

    /**
     * Create equal Compare Condition Expression Executor which evaluates whether value of leftExpressionExecutor
     * is equal to value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseEqualCompare(ExpressionExecutor leftExpressionExecutor,
                                                                 ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        return new EqualCompareConditionExpressionExecutorStringString(leftExpressionExecutor,
                                rightExpressionExecutor);
                    default:
                        throw new OperationNotSupportedException(
                                "string cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new EqualCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new EqualCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new EqualCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new EqualCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new EqualCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new EqualCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new EqualCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new EqualCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new EqualCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new EqualCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new EqualCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new EqualCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new EqualCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new EqualCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new EqualCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new EqualCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                switch (rightExpressionExecutor.getReturnType()) {
                    case BOOL:
                        return new EqualCompareConditionExpressionExecutorBoolBool(leftExpressionExecutor,
                                rightExpressionExecutor);
                    default:
                        throw new OperationNotSupportedException(
                                "bool cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " equal comparisons");
        }
    }

    /**
     * Create not equal Compare Condition Expression Executor which evaluates whether value of leftExpressionExecutor
     * is not equal to value of rightExpressionExecutor.
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Condition ExpressionExecutor
     */
    private static ConditionExpressionExecutor parseNotEqualCompare(ExpressionExecutor leftExpressionExecutor,
                                                                    ExpressionExecutor rightExpressionExecutor) {
        switch (leftExpressionExecutor.getReturnType()) {
            case STRING:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        return new NotEqualCompareConditionExpressionExecutorStringString(leftExpressionExecutor,
                                rightExpressionExecutor);
                    default:
                        throw new OperationNotSupportedException(
                                "string cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case INT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("int cannot be compared with string");
                    case INT:
                        return new NotEqualCompareConditionExpressionExecutorIntInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new NotEqualCompareConditionExpressionExecutorIntLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new NotEqualCompareConditionExpressionExecutorIntFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new NotEqualCompareConditionExpressionExecutorIntDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("int cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "int cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case LONG:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("long cannot be compared with string");
                    case INT:
                        return new NotEqualCompareConditionExpressionExecutorLongInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new NotEqualCompareConditionExpressionExecutorLongLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new NotEqualCompareConditionExpressionExecutorLongFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new NotEqualCompareConditionExpressionExecutorLongDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("long cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "long cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case FLOAT:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("float cannot be compared with string");
                    case INT:
                        return new NotEqualCompareConditionExpressionExecutorFloatInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new NotEqualCompareConditionExpressionExecutorFloatLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new NotEqualCompareConditionExpressionExecutorFloatFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new NotEqualCompareConditionExpressionExecutorFloatDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("float cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "float cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case DOUBLE:
                switch (rightExpressionExecutor.getReturnType()) {
                    case STRING:
                        throw new OperationNotSupportedException("double cannot be compared with string");
                    case INT:
                        return new NotEqualCompareConditionExpressionExecutorDoubleInt(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case LONG:
                        return new NotEqualCompareConditionExpressionExecutorDoubleLong(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case FLOAT:
                        return new NotEqualCompareConditionExpressionExecutorDoubleFloat(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case DOUBLE:
                        return new NotEqualCompareConditionExpressionExecutorDoubleDouble(leftExpressionExecutor,
                                rightExpressionExecutor);
                    case BOOL:
                        throw new OperationNotSupportedException("double cannot be compared with bool");
                    default:
                        throw new OperationNotSupportedException(
                                "double cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            case BOOL:
                switch (rightExpressionExecutor.getReturnType()) {
                    case BOOL:
                        return new NotEqualCompareConditionExpressionExecutorBoolBool(leftExpressionExecutor,
                                rightExpressionExecutor);
                    default:
                        throw new OperationNotSupportedException(
                                "bool cannot be compared with " + rightExpressionExecutor.getReturnType());
                }
            default:
                throw new OperationNotSupportedException(
                        leftExpressionExecutor.getReturnType() + " cannot be used in" + " not equal comparisons");
        }
    }

    /**
     * Parse and validate the given Siddhi variable and return a VariableExpressionExecutor
     *
     * @param variable           Variable to be parsed
     * @param metaEvent          Meta event used to collect execution info of stream associated with query
     * @param currentState       Current State Number
     * @param executorList       List to hold VariableExpressionExecutors to update after query parsing @return
     * @param siddhiQueryContext Siddhi Query Context
     * @return VariableExpressionExecutor representing given variable
     */
    private static ExpressionExecutor parseVariable(Variable variable, MetaComplexEvent metaEvent,
                                                    int currentState,
                                                    List<VariableExpressionExecutor> executorList,
                                                    int defaultStreamEventIndex,
                                                    SiddhiQueryContext siddhiQueryContext) {
        String attributeName = variable.getAttributeName();
        int[] eventPosition = new int[2];
        if (variable.getStreamIndex() != null) {
            if (variable.getStreamIndex() <= SiddhiConstants.LAST) {
                eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = variable.getStreamIndex() + 1;
            } else {
                eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = variable.getStreamIndex();
            }
        } else {
            eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] = defaultStreamEventIndex;
        }
        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = SiddhiConstants.UNKNOWN_STATE;
        if (metaEvent instanceof MetaStreamEvent) {
            MetaStreamEvent metaStreamEvent = (MetaStreamEvent) metaEvent;
            AbstractDefinition abstractDefinition;
            Attribute.Type type;
            if (currentState == SiddhiConstants.HAVING_STATE) {
                abstractDefinition = metaStreamEvent.getOutputStreamDefinition();
                type = abstractDefinition.getAttributeType(attributeName);
                eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = SiddhiConstants.HAVING_STATE;
            } else {
                abstractDefinition = metaStreamEvent.getLastInputDefinition();
                type = abstractDefinition.getAttributeType(attributeName);
                ((MetaStreamEvent) metaEvent).addData(new Attribute(attributeName, type));
            }
            if (variable.getStreamId() != null && !variable.getStreamId().equals(abstractDefinition.getId())) {
                throw new SiddhiAppCreationException("Id '" + variable.getStreamId() + "' not defined within the " +
                        "current scope", variable, siddhiQueryContext.getSiddhiAppContext());
            }
            VariableExpressionExecutor variableExpressionExecutor = new VariableExpressionExecutor(
                    new Attribute(attributeName, type), eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX],
                    eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN]);
            if (((MetaStreamEvent) metaEvent).getEventType() != MetaStreamEvent.EventType.DEFAULT) {
                variableExpressionExecutor.getPosition()[SiddhiConstants.STREAM_ATTRIBUTE_TYPE_INDEX] =
                        SiddhiConstants.OUTPUT_DATA_INDEX;
                variableExpressionExecutor.getPosition()[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE] =
                        abstractDefinition
                                .getAttributePosition(variableExpressionExecutor.getAttribute().getName());
            }
            if (executorList != null) {
                executorList.add(variableExpressionExecutor);
            }
            return variableExpressionExecutor;
        } else {
            MetaStateEvent metaStateEvent = (MetaStateEvent) metaEvent;
            Attribute.Type type = null;
            AbstractDefinition definition = null;
            String firstInput = null;
            boolean multiValue = false;
            if (variable.getStreamId() == null) {
                MetaStreamEvent[] metaStreamEvents = metaStateEvent.getMetaStreamEvents();
                if (currentState == SiddhiConstants.HAVING_STATE) {
                    definition = metaStateEvent.getOutputStreamDefinition();
                    try {
                        type = definition.getAttributeType(attributeName);
                        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = SiddhiConstants.HAVING_STATE;
                    } catch (AttributeNotExistException e) {
                        currentState = SiddhiConstants.UNKNOWN_STATE;
                    }
                }
                if (currentState == SiddhiConstants.UNKNOWN_STATE) {
                    for (int i = 0; i < metaStreamEvents.length; i++) {
                        MetaStreamEvent metaStreamEvent = metaStreamEvents[i];
                        definition = metaStreamEvent.getLastInputDefinition();
                        if (type == null) {
                            try {
                                type = definition.getAttributeType(attributeName);
                                firstInput = "Input Stream: " + definition.getId() + " with " + "reference: "
                                        + metaStreamEvent.getInputReferenceId();
                                eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                            } catch (AttributeNotExistException e) {
                                // do nothing
                            }
                        } else {
                            try {
                                definition.getAttributeType(attributeName);
                                throw new SiddhiAppValidationException(firstInput + " and Input Stream: "
                                        + definition.getId() + " with " + "reference: "
                                        + metaStreamEvent.getInputReferenceId() + " contains attribute " + "with same"
                                        + " name '" + attributeName + "'");
                            } catch (AttributeNotExistException e) {
                                // do nothing as its expected
                            }
                        }
                    }
                } else if (currentState >= 0) {
                    MetaStreamEvent metaStreamEvent = metaStreamEvents[currentState];
                    definition = metaStreamEvent.getLastInputDefinition();
                    try {
                        type = definition.getAttributeType(attributeName);
                        eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = currentState;
                    } catch (AttributeNotExistException e) {
                        ExpressionExecutor functionExecutor = constructEventExpressionExecutor(variable, currentState,
                                siddhiQueryContext, eventPosition, metaStateEvent);
                        if (functionExecutor != null) {
                            return functionExecutor;
                        }
                        throw new SiddhiAppValidationException(e.getMessageWithOutContext() + " Input stream '" +
                                definition.getId() + "' with reference '" +
                                metaStreamEvent.getInputReferenceId() + "' for attribute '" +
                                variable.getAttributeName() + "'", e,
                                e.getQueryContextStartIndex(), e.getQueryContextEndIndex());
                    }
                }
            } else {
                MetaStreamEvent[] metaStreamEvents = metaStateEvent.getMetaStreamEvents();
                for (int i = 0, metaStreamEventsLength = metaStreamEvents.length; i < metaStreamEventsLength; i++) {
                    MetaStreamEvent metaStreamEvent = metaStreamEvents[i];
                    definition = metaStreamEvent.getLastInputDefinition();
                    if (metaStreamEvent.getInputReferenceId() == null) {
                        if (definition.getId().equals(variable.getStreamId())) {
                            type = definition.getAttributeType(attributeName);
                            eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                            break;
                        }
                    } else {
                        if (metaStreamEvent.getInputReferenceId().equals(variable.getStreamId())) {
                            type = definition.getAttributeType(attributeName);
                            eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                            if (currentState > -1 && metaStreamEvents[currentState].getInputReferenceId() != null
                                    && variable.getStreamIndex() != null
                                    && variable.getStreamIndex() <= SiddhiConstants.LAST) {
                                if (variable.getStreamId()
                                        .equals(metaStreamEvents[currentState].getInputReferenceId())) {
                                    eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] =
                                            variable.getStreamIndex();
                                }
                            } else if (currentState == SiddhiConstants.UNKNOWN_STATE &&
                                    variable.getStreamIndex() == null) {
                                multiValue = metaStreamEvent.isMultiValue();
                            }
                            break;
                        }
                    }
                }
            }
            if (eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] == SiddhiConstants.UNKNOWN_STATE) {
                if (variable.getStreamId() == null) {
                    ExpressionExecutor functionExecutor = constructEventExpressionExecutor(variable, currentState, siddhiQueryContext, eventPosition, metaStateEvent);
                    if (functionExecutor != null) {
                        return functionExecutor;
                    }
                    throw new SiddhiAppValidationException(
                            "No matching stream reference found for attribute '" + variable.getAttributeName() + "'");
                } else {
                    throw new SiddhiAppValidationException(
                            "Stream with reference '" + variable.getStreamId() + "' not found for attribute '" +
                                    variable.getAttributeName() + "'");
                }
            }
            VariableExpressionExecutor variableExpressionExecutor = new VariableExpressionExecutor(
                    new Attribute(attributeName, type), eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX],
                    eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN]);
            if (eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] != SiddhiConstants.HAVING_STATE) {
                MetaStreamEvent metaStreamEvent = ((MetaStateEvent) metaEvent)
                        .getMetaStreamEvent(eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX]);
                if (metaStreamEvent.getEventType() != MetaStreamEvent.EventType.DEFAULT) {
                    variableExpressionExecutor.getPosition()[SiddhiConstants.STREAM_ATTRIBUTE_TYPE_INDEX] =
                            SiddhiConstants.OUTPUT_DATA_INDEX;
                    variableExpressionExecutor.getPosition()[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE] =
                            metaStreamEvent.getLastInputDefinition()
                                    .getAttributePosition(variableExpressionExecutor.getAttribute().getName());
                    for (Attribute attribute : metaStreamEvent.getLastInputDefinition().getAttributeList()) {
                        metaStreamEvent.addOutputData(new Attribute(attribute.getName(), attribute.getType()));
                    }
                }
                metaStreamEvent.addData(new Attribute(attributeName, type));
            }
            if (executorList != null) {
                executorList.add(variableExpressionExecutor);
            }
            if (multiValue) {
                FunctionExecutor functionExecutor = new MultiValueVariableFunctionExecutor();
                ExpressionExecutor[] innerExpressionExecutors = new ExpressionExecutor[]{variableExpressionExecutor};
                functionExecutor.initExecutor(innerExpressionExecutors,
                        ProcessingMode.BATCH, null, false, siddhiQueryContext);
                return functionExecutor;
            }
            return variableExpressionExecutor;
        }
    }

    private static ExpressionExecutor constructEventExpressionExecutor(Variable variable, int currentState, SiddhiQueryContext siddhiQueryContext, int[] eventPosition, MetaStateEvent metaStateEvent) {
        AbstractDefinition definition;
        MetaStreamEvent[] metaStreamEvents = metaStateEvent.getMetaStreamEvents();
        for (int i = 0, metaStreamEventsLength = metaStreamEvents.length; i < metaStreamEventsLength; i++) {
            MetaStreamEvent metaStreamEvent = metaStreamEvents[i];
            definition = metaStreamEvent.getLastInputDefinition();
            if (metaStreamEvent.getInputReferenceId() == null) {
                if (definition.getId().equals(variable.getAttributeName())) {
                    eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                    FunctionExecutor functionExecutor = new EventVariableFunctionExecutor(
                            eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX],
                            eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN]);
                    functionExecutor.initExecutor(new ExpressionExecutor[0], ProcessingMode.BATCH,
                            null, false, siddhiQueryContext);
                    return functionExecutor;
                }
            } else {
                if (metaStreamEvent.getInputReferenceId().equals(variable.getAttributeName())) {
                    eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX] = i;
                    if (currentState > -1 && metaStreamEvents[currentState].getInputReferenceId() != null
                            && variable.getStreamIndex() != null
                            && variable.getStreamIndex() <= SiddhiConstants.LAST) {
                        if (variable.getAttributeName()
                                .equals(metaStreamEvents[currentState].getInputReferenceId())) {
                            eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN] =
                                    variable.getStreamIndex();
                        }
                    }
                    FunctionExecutor functionExecutor = new EventVariableFunctionExecutor(
                            eventPosition[SiddhiConstants.STREAM_EVENT_CHAIN_INDEX],
                            eventPosition[SiddhiConstants.STREAM_EVENT_INDEX_IN_CHAIN]);
                    functionExecutor.initExecutor(new ExpressionExecutor[0], ProcessingMode.BATCH,
                            null, false, siddhiQueryContext);
                    return functionExecutor;
                }
            }
        }
        return null;
    }

    /**
     * Calculate the return type of arithmetic operation executors.(Ex: add, subtract, etc)
     *
     * @param leftExpressionExecutor  left ExpressionExecutor
     * @param rightExpressionExecutor right ExpressionExecutor
     * @return Attribute Type
     */
    private static Attribute.Type parseArithmeticOperationResultType(ExpressionExecutor leftExpressionExecutor,
                                                                     ExpressionExecutor rightExpressionExecutor) {
        if (leftExpressionExecutor.getReturnType() == Attribute.Type.DOUBLE
                || rightExpressionExecutor.getReturnType() == Attribute.Type.DOUBLE) {
            return Attribute.Type.DOUBLE;
        } else if (leftExpressionExecutor.getReturnType() == Attribute.Type.FLOAT
                || rightExpressionExecutor.getReturnType() == Attribute.Type.FLOAT) {
            return Attribute.Type.FLOAT;
        } else if (leftExpressionExecutor.getReturnType() == Attribute.Type.LONG
                || rightExpressionExecutor.getReturnType() == Attribute.Type.LONG) {
            return Attribute.Type.LONG;
        } else if (leftExpressionExecutor.getReturnType() == Attribute.Type.INT
                || rightExpressionExecutor.getReturnType() == Attribute.Type.INT) {
            return Attribute.Type.INT;
        } else {
            throw new ArithmeticException("Arithmetic operation between " + leftExpressionExecutor.getReturnType()
                    + " and " + rightExpressionExecutor.getReturnType() + " cannot be executed");
        }
    }

    /**
     * Parse the set of inner expression of AttributeFunctionExtensions and handling all (*) cases
     *
     * @param innerExpressions           InnerExpressions to be parsed
     * @param metaEvent                  Meta Event
     * @param currentState               Current state number
     * @param tableMap                   Event Table Map
     * @param executorList               List to hold VariableExpressionExecutors to update after query parsing @return
     * @param groupBy                    is for groupBy expression
     * @param defaultStreamEventIndex    Default StreamEvent Index
     * @param processingMode             processing mode of the query
     * @param outputExpectsExpiredEvents is expired events sent as output
     * @param siddhiQueryContext         current siddhi query context
     * @return List of expressionExecutors
     */
    private static ExpressionExecutor[] parseInnerExpression(Expression[] innerExpressions, MetaComplexEvent metaEvent,
                                                             int currentState, Map<String, Table> tableMap,
                                                             List<VariableExpressionExecutor> executorList,
                                                             boolean groupBy,
                                                             int defaultStreamEventIndex,
                                                             ProcessingMode processingMode,
                                                             boolean outputExpectsExpiredEvents,
                                                             SiddhiQueryContext siddhiQueryContext) {
        ExpressionExecutor[] innerExpressionExecutors;
        if (innerExpressions != null) {
            if (innerExpressions.length > 0) {
                innerExpressionExecutors = new ExpressionExecutor[innerExpressions.length];
                for (int i = 0, innerExpressionsLength = innerExpressions.length; i < innerExpressionsLength; i++) {
                    innerExpressionExecutors[i] = parseExpression(innerExpressions[i], metaEvent, currentState,
                            tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                }
            } else {
                List<Expression> outputAttributes = new ArrayList<Expression>();
                if (metaEvent instanceof MetaStreamEvent) {

                    List<Attribute> attributeList = ((MetaStreamEvent) metaEvent).getLastInputDefinition()
                            .getAttributeList();
                    for (Attribute attribute : attributeList) {
                        outputAttributes.add(new Variable(attribute.getName()));
                    }
                } else {
                    for (MetaStreamEvent metaStreamEvent : ((MetaStateEvent) metaEvent).getMetaStreamEvents()) {
                        List<Attribute> attributeList = metaStreamEvent.getLastInputDefinition().getAttributeList();
                        for (Attribute attribute : attributeList) {
                            Expression outputAttribute = new Variable(attribute.getName());
                            if (!outputAttributes.contains(outputAttribute)) {
                                outputAttributes.add(outputAttribute);
                            } else {
                                List<AbstractDefinition> definitions = new ArrayList<AbstractDefinition>();
                                for (MetaStreamEvent aMetaStreamEvent : ((MetaStateEvent) metaEvent)
                                        .getMetaStreamEvents()) {
                                    definitions.add(aMetaStreamEvent.getLastInputDefinition());
                                }
                                throw new DuplicateAttributeException(
                                        "Duplicate attribute exist in streams " + definitions,
                                        attribute.getQueryContextStartIndex(), attribute.getQueryContextEndIndex());
                            }
                        }
                    }
                }
                innerExpressionExecutors = new ExpressionExecutor[outputAttributes.size()];
                for (int i = 0, innerExpressionsLength = outputAttributes.size(); i < innerExpressionsLength; i++) {
                    innerExpressionExecutors[i] = parseExpression(outputAttributes.get(i), metaEvent, currentState,
                            tableMap, executorList, groupBy, defaultStreamEventIndex,
                            processingMode, outputExpectsExpiredEvents, siddhiQueryContext);
                }
            }
        } else {
            innerExpressionExecutors = new ExpressionExecutor[0];
        }
        return innerExpressionExecutors;
    }

}
