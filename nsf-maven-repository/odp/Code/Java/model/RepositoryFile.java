package model;

import org.eclipse.jnosql.communication.driver.attachment.EntityAttachment;
import org.openntf.xsp.jakarta.nosql.mapping.extension.DominoRepository;
import org.openntf.xsp.jakarta.nosql.mapping.extension.ItemFlags;
import org.openntf.xsp.jakarta.nosql.mapping.extension.ItemStorage;
import org.openntf.xsp.jakarta.nosql.communication.driver.DominoConstants;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity("File")
public class RepositoryFile {
	public interface Repository extends DominoRepository<RepositoryFile, String> {
		Optional<RepositoryFile> findByParentAndName(String parent, String name);
		
		Stream<RepositoryFile> findByParent(String parent);
	}
	
	@Id private String documentId;
	@Column("Parent") private String parent;
	@Column("$$Title") private String name;
	@Column("Owner") @ItemFlags(names = true) private String owner;
	@Column("Group") @ItemFlags(names = true) private String group;
	@Column("Permissions") private String permissions;
	@Column(DominoConstants.FIELD_ATTACHMENTS) private List<EntityAttachment> attachments;
	// Stub for compatibility with existing versions of NSF File Server
	@Column("File") @ItemStorage(type = ItemStorage.Type.MIME) private String file;
	
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
	public List<EntityAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EntityAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}
