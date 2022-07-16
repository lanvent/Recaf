package me.coley.recaf.parse.jpimpl;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import me.coley.recaf.code.CommonClassInfo;
import me.coley.recaf.code.FieldInfo;
import me.coley.recaf.parse.WorkspaceTypeSolver;
import me.coley.recaf.util.AccessFlag;

import java.util.List;
import java.util.stream.Collectors;

public class RecafResolvedEnumDeclaration extends RecafResolvedTypeDeclaration implements ResolvedEnumDeclaration {
	public RecafResolvedEnumDeclaration(WorkspaceTypeSolver typeSolver, CommonClassInfo classInfo) {
		super(typeSolver, classInfo);
	}

	@Override
	public List<ResolvedEnumConstantDeclaration> getEnumConstants() {
		return classInfo.getFields().stream()
				.filter(f -> AccessFlag.hasAll(f.getAccess(), AccessFlag.ACC_PUBLIC, AccessFlag.ACC_STATIC) &&
						isDescriptorOfInternalType(f.getDescriptor(), classInfo.getName()))
				.map(f -> new RecafResolvedEnumConstantDeclaration(this, f))
				.collect(Collectors.toList());
	}

	@Override
	public AccessSpecifier accessSpecifier() {
		return super.accessSpecifier();
	}

	private static boolean isDescriptorOfInternalType(String descriptor, String internal) {
		return descriptor.substring(1, descriptor.length() - 1).equals(internal);
	}

	private static class RecafResolvedEnumConstantDeclaration extends RecafResolvedFieldDeclaration implements ResolvedEnumConstantDeclaration {
		public RecafResolvedEnumConstantDeclaration(RecafResolvedTypeDeclaration declaringType, FieldInfo fieldInfo) {
			super(declaringType, fieldInfo);
		}
	}
}
