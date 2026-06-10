package api.maven;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.jnosql.communication.driver.attachment.EntityAttachment;
import org.openntf.xsp.jakarta.nosql.communication.driver.ByteArrayEntityAttachment;

import com.ibm.commons.util.StringUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.Folder;
import model.RepositoryFile;
import util.AppPathUtil;

@Path("repository")
public class RepositoryService {
	private static final Logger log = System.getLogger(RepositoryService.class.getPackageName());
	
	@Inject
	private RepositoryFile.Repository filesRepository;
	
	@Inject
	private Folder.Repository folderRepository;
	
	// TODO condense the maven-metadata.xml so that they can parse the XML and determine
	//      whether it's supposed to be a groupId or an artifact
	@Path("{groupPath:.+}/maven-metadata.xml{ext:.*}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public InputStream getGroupMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("ext") String ext) {
		return findFile(groupPath, artifactId, "", "maven-metadata.xml" + ext)
			.orElseThrow(() -> new NotFoundException());
	}
	
	@Path("{groupPath:.+}/maven-metadata.xml{ext:.*}")
	@PUT
	public void putGroupMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("ext") String ext, byte[] data) {
		saveFile(groupPath, artifactId, "", "maven-metadata.xml" + ext, data);
	}
	
	@Path("{groupPath:.+}/{artifactId}/maven-metadata.xml{ext:.*}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public InputStream getArtifactMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("ext") String ext) {
		return findFile(groupPath, artifactId, "", "maven-metadata.xml" + ext)
			.orElseThrow(() -> new NotFoundException());
	}
	
	@Path("{groupPath:.+}/{artifactId}/maven-metadata.xml{ext:.*}")
	@PUT
	public void putArtifactMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("ext") String ext, byte[] data) {
		saveFile(groupPath, artifactId, "", "maven-metadata.xml" + ext, data);
	}
	
	@Path("{groupPath:.+}/{artifactId}/{version}/maven-metadata.xml{ext:.*}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public InputStream getVersionMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("version") String version, @PathParam("ext") String ext) {
		return findFile(groupPath, artifactId, version, "maven-metadata.xml" + ext)
				.orElseThrow(() -> new NotFoundException());
	}
	
	@Path("{groupPath:.+}/{artifactId}/{version}/maven-metadata.xml{ext:.*}")
	@PUT
	public void putVersionMetadata(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("version") String version, @PathParam("ext") String ext, byte[] data) {
		saveFile(groupPath, artifactId, version, "maven-metadata.xml" + ext, data);
	}
	
	@Path("{groupPath:.+}/{artifactId}/{version}/{fileName}")
	@GET
	public InputStream getFile(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("version") String version, @PathParam("fileName") String fileName) {
		return findFile(groupPath, artifactId, version, fileName)
			.orElseThrow(() -> new NotFoundException());
	}
	
	@Path("{groupPath:.+}/{artifactId}/{version}/{fileName}")
	@PUT
	public void putFile(@PathParam("groupPath") String groupPath, @PathParam("artifactId") String artifactId, @PathParam("version") String version, @PathParam("fileName") String fileName, byte[] data) {
		saveFile(groupPath, artifactId, version, fileName, data);
	}
	
	private Optional<InputStream> findFile(String groupPath, String artifactId, String version, String fileName) {
		String parentPath = AppPathUtil.concat('/', "/", groupPath, artifactId, version);
		return filesRepository.findByParentAndName(parentPath, fileName)
			.map(RepositoryFile::getAttachments)
			.map(attachments -> attachments.get(0))
			.map(att -> {
				try {
					return att.getData();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
	}
	
	private void saveFile(String groupPath, String artifactId, String version, String fileName, byte[] data) {
		try {
			String parentPath = AppPathUtil.concat('/', "/", groupPath, artifactId, version);
			// Make sure the parents exist for consistency
			{
				String folderParentPath = null;
				String folderPath = "/";
				String[] parts = StringUtil.splitString(parentPath, '/');
				// Skip the initial ""
				for(int i = 1; i < parts.length; i++) {
					folderParentPath = folderPath;
					folderPath = AppPathUtil.concat('/', folderPath, parts[i]);
					Optional<Folder> optFolder = folderRepository.findByParentAndName(folderParentPath, parts[i]);
					if(!optFolder.isPresent()) {
						//System.out.println("making new folder parent=" + folderParentPath + "; name=" + parts[i]);
						Folder folder = new Folder();
						folder.setParent(folderParentPath);
						folder.setName(parts[i]);
						folderRepository.save(folder, true);
					}
				}
			}
	
			
			RepositoryFile file = filesRepository.findByParentAndName(parentPath, fileName).orElseGet(RepositoryFile::new);
			
			List<EntityAttachment> attachments = file.getAttachments();
			if(attachments != null && !attachments.isEmpty()) {
				// Re-save the doc since the file name will likely be the same,
				//   which is our distinguishing element for the Domino driver
				file.setAttachments(Collections.emptyList());
				filesRepository.save(file, true);
			}
	
			log.log(Level.DEBUG, () -> MessageFormat.format("Saving file parent={0}, name={1}, new={2}", parentPath, fileName, StringUtil.isEmpty(file.getDocumentId())));
			file.setParent(parentPath);
			file.setName(fileName);
			file.setAttachments(Collections.singletonList(new ByteArrayEntityAttachment(fileName, "application/octet-stream", System.currentTimeMillis(), data)));
			file.setFile("Stub content for compatibility with NSF File Server");
			filesRepository.save(file, true);
		} catch(Exception e) {
			log.log(Level.ERROR, () -> MessageFormat.format("Saving file groupPath={0}, artifactId={1}, version={2}, fileName={3}", groupPath, artifactId, version, fileName), e);
			throw e;
		}
	}
}
