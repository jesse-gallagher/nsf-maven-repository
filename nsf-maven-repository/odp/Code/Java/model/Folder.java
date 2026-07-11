package model;

import java.util.Optional;
import java.util.stream.Stream;

import org.openntf.xsp.jakarta.nosql.mapping.extension.DominoRepository;
import org.openntf.xsp.jakarta.nosql.mapping.extension.ItemFlags;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity("Folder")
public class Folder {
	public interface Repository extends DominoRepository<Folder, String> {
		Optional<Folder> findByParentAndName(String parent, String name);
		
		Stream<Folder> findByParent(String parent);
	}
	

	@Id private String documentId;
	@Column("Parent") private String parent;
	@Column("$$Title") private String name;
	@Column("Owner") @ItemFlags(names = true) private String owner;
	@Column("Group") @ItemFlags(names = true) private String group;
	@Column("Permissions") private String permissions;
	
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
}
