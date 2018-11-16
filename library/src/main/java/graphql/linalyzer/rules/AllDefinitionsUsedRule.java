package graphql.linalyzer.rules;

import graphql.language.NamedNode;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.Severity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AllDefinitionsUsedRule implements LinterRule {


    private final String ruleId;
    private final Severity severity;

    public AllDefinitionsUsedRule(String ruleId, Severity severity) {
        this.ruleId = ruleId;
        this.severity = severity;
    }

    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        List<NamedNode> nodes = schemaDefinition.getAllNodes(Arrays.asList(
                SchemaDefinitionElement.INPUT,
                SchemaDefinitionElement.INTERFACE,
                SchemaDefinitionElement.OBJECT,
                SchemaDefinitionElement.UNION,
                SchemaDefinitionElement.CUSTOM_SCALAR,
                SchemaDefinitionElement.ENUM,
                SchemaDefinitionElement.TYPE_NAME
        ));


        List<TypeName> typeNames = nodes
                .stream()
                .filter(TypeName.class::isInstance)
                .map(TypeName.class::cast)
                .collect(Collectors.toList());

        List<NamedNode> definedTypes = nodes
                .stream()
                .filter(node -> !(node instanceof TypeName))
                .collect(Collectors.toList());

        Set<String> allTypeNames = typeNames
                .stream()
                .map(TypeName::getName)
                .collect(Collectors.toSet());

        return definedTypes
                .stream()
                .filter(node -> !allTypeNames.contains(node.getName()))
                .filter(namedNode -> filterTopLevelTypes(namedNode, schemaDefinition))
                .map(this::createRuleResult)
                .collect(Collectors.toList());
    }

    private boolean filterTopLevelTypes(NamedNode definition, SchemaDefinition schemaDefinition) {
        if (schemaDefinition.getQueryDefinition().getName().equals(definition.getName())) {
            return false;
        }
        Optional<ObjectTypeDefinition> mutationDefinition = schemaDefinition.getMutationDefinition();
        if (mutationDefinition.isPresent() && mutationDefinition.get().getName().equals(definition.getName())) {
            return false;
        }
        Optional<ObjectTypeDefinition> subscriptionDefinition = schemaDefinition.getSubscriptionDefinition();
        if (subscriptionDefinition.isPresent() && subscriptionDefinition.get().getName().equals(definition.getName())) {
            return false;
        }
        return true;
    }

    private LinterRuleResult createRuleResult(NamedNode unusedDefinition) {
        return new LinterRuleResult(severity, ruleId,
                unusedDefinition.getSourceLocation(),
                "Unused definition " + unusedDefinition.getName() + ".");

    }
}
