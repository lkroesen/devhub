package nl.tudelft.ewi.devhub.server.database.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "groups")
@EqualsAndHashCode(of = { "groupId" })
public class Group {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long groupId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@NotNull
	@Column(name = "group_number")
	private long groupNumber;
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private Set<GroupMembership> memberships;

	public String getRepoName() {
		return "courses/" + getCourse().getCode().toLowerCase() + "/group-" + getGroupNumber();
	}

}