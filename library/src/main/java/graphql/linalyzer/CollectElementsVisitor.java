package graphql.linalyzer;

import graphql.language.EnumTypeDefinition;
import graphql.language.FieldDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.NamedNode;
import graphql.language.Node;
import graphql.language.NodeVisitorStub;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import graphql.language.UnionTypeDefinition;
import graphql.util.TraversalControl;
import graphql.util.TraverserContext;

import java.util.ArrayList;
import java.util.List;

public class CollectElementsVisitor extends NodeVisitorStub {

    private final List<SchemaDefinitionElement> schemaDefinitionElements;
    private final List<NamedNode> result = new ArrayList<>();

    public CollectElementsVisitor(List<SchemaDefinitionElement> schemaDefinitionElements) {
        this.schemaDefinitionElements = schemaDefinitionElements;
    }

    public List<NamedNode> getResult() {
        return result;
    }

    @Override
    public TraversalControl visitFieldDefinition(FieldDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.FIELD)) {
            result.add(node);
        }
        return super.visitFieldDefinition(node, context);
    }

    @Override
    public TraversalControl visitObjectTypeDefinition(ObjectTypeDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.OBJECT)) {
            result.add(node);
        }
        return super.visitObjectTypeDefinition(node, context);
    }

    @Override
    public TraversalControl visitInterfaceTypeDefinition(InterfaceTypeDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.INTERFACE)) {
            result.add(node);
        }
        return super.visitInterfaceTypeDefinition(node, context);
    }

    @Override
    public TraversalControl visitEnumTypeDefinition(EnumTypeDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.ENUM)) {
            result.add(node);
        }
        return super.visitEnumTypeDefinition(node, context);
    }

    @Override
    public TraversalControl visitInputObjectTypeDefinition(InputObjectTypeDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.INPUT)) {
            result.add(node);
        }
        return super.visitInputObjectTypeDefinition(node, context);
    }

    @Override
    public TraversalControl visitUnionTypeDefinition(UnionTypeDefinition node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.UNION)) {
            result.add(node);
        }
        return super.visitUnionTypeDefinition(node, context);
    }

    @Override
    public TraversalControl visitTypeName(TypeName node, TraverserContext<Node> context) {
        if (schemaDefinitionElements.contains(SchemaDefinitionElement.TYPE_NAME)) {
            result.add(node);
        }
        return super.visitTypeName(node, context);
    }
}
