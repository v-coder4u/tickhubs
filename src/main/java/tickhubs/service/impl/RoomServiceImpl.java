package tickhubs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tickhubs.dto.RoomRequest;
import tickhubs.dto.RoomResponse;
import tickhubs.enums.RoomType;
import tickhubs.model.Room;
import tickhubs.model.User;
import tickhubs.repository.PollRepository;
import tickhubs.repository.RoomRepository;
import tickhubs.repository.TagRepository;
import tickhubs.repository.UserRepository;
import tickhubs.repository.VoteRepository;
import tickhubs.security.UserPrincipal;

@Service
public class RoomServiceImpl {

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private RoomRepository roomRepository;

	public Room createRoom(RoomRequest roomRequest) {
		Room room = Room.builder().name(roomRequest.getName()).description(roomRequest.getDescription())
				.website(roomRequest.getWebsite()).createdByUser(roomRequest.getUserId()).build();
		if (roomRequest.getType().equalsIgnoreCase(RoomType.PRIVATE.getName())) {
			room.setRoomType(RoomType.PRIVATE.getName());
		} else if (roomRequest.getType().equalsIgnoreCase(RoomType.PUBLIC.getName())) {
			room.setRoomType(RoomType.PUBLIC.getName());
		} else {
			room.setRoomType("None");
		}
		return roomRepository.save(room);
	}

	public RoomResponse getPollByUsernameOrRoomName(Long name, UserPrincipal currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
