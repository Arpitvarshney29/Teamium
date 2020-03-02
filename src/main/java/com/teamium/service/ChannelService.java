package com.teamium.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.domain.Channel;
import com.teamium.domain.ChannelFormat;
import com.teamium.domain.Format;
import com.teamium.dto.ChannelDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.ChannelFormatRepository;
import com.teamium.repository.ChannelRepository;

/**
 * A service class implementation for channel controller
 *
 */

@Service
public class ChannelService {

	private ChannelRepository channelRepository;
	private FormatService formatService;
	private ChannelFormatRepository channelFormatRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param channelRepository
	 */
	public ChannelService(ChannelRepository channelRepository, FormatService formatService,
			ChannelFormatRepository channelFormatRepository) {
		this.formatService = formatService;
		this.channelRepository = channelRepository;
		this.channelFormatRepository = channelFormatRepository;
	}

	/**
	 * To get list of channels
	 * 
	 * @return list of ChannelDTO
	 */
	public List<ChannelDTO> getChannels() {
		logger.info("Inside getChannels().");
		List<ChannelDTO> channels = channelRepository.findAll().stream().map(ch -> new ChannelDTO(ch, "ForList"))
				.collect(Collectors.toList());
		logger.info(channels.size() + " channels found ");
		logger.info("Returning from getChannels()");
		return channels;
	}

	/**
	 * To get channel by channelId .
	 * 
	 * @param channelId the channelId.
	 * 
	 * @return the ChannelDTO.
	 */
	public ChannelDTO getChannel(Long channelId) {
		logger.info("Inside getChannel() :: getting channel with id: " + channelId);
		ChannelDTO channelDto = new ChannelDTO(findChannelById(channelId), "ForLisT");
		logger.info("Returning from getChannel() .");
		return channelDto;
	}

	/**
	 * To find channel by id.
	 * 
	 * @param channelId the channelId.
	 * 
	 * @return the channel.
	 */
	public Channel findChannelById(long channelId) {
		logger.info("Inside  findChannelById()  :: finding channel with id " + channelId);
		Channel channel = channelRepository.findOne(channelId);
		if (channel == null) {
			logger.error("Invalid channel id.");
			throw new NotFoundException("Invalid channel id.");
		}
		logger.info("Returning from findChannelById() .");
		return channel;
	}

	/**
	 * To delete channel.
	 * 
	 * @param channelId the channelId.
	 */
	public void deleteChannel(long channelId) {
		logger.info("Inside deleteChannel() :: deleting channel with id: " + channelId);
		findChannelById(channelId);
		channelRepository.delete(channelId);
		logger.info("Returning from deleteChannel().");
	}

	/**
	 * To save or update channel
	 * 
	 * @param channelDTO
	 * @return
	 */
	public ChannelDTO saveOrUpdateChannel(ChannelDTO channelDTO) {
		logger.info("Inside ChannelService :: saveOrUpdateChannel channel: " + channelDTO);
		validateChannelSave(channelDTO);
		Channel channel = new Channel();
		if (channelDTO.getId() != null) {
			channel = findChannelById(channelDTO.getId());
		}
		validateChannelFormats(channelDTO.getFormats());
		channel.setName(channelDTO.getName());

		Set<Format> dbFormats = channel.getFormats();
		Set<Format> dtoFormats = channelDTO.getFormats();

		if ((dbFormats == null || dbFormats.isEmpty()) && (dtoFormats != null && !dtoFormats.isEmpty())) {
			channel.setFormats(channelDTO.getFormats());
		} else if ((dbFormats != null && !dbFormats.isEmpty()) && (dtoFormats == null || dtoFormats.isEmpty())) {
			channel.getFormats().clear();
		} else {
			List<Long> dbIDs = dbFormats.stream().map(form -> form.getId()).collect(Collectors.toList());
			List<Long> dtoIDs = dtoFormats.stream().map(form -> form.getId()).collect(Collectors.toList());
			dbIDs.stream().forEach(id -> {
				if (!dtoIDs.contains(id)) {
					Format format = formatService.getFormatById(id);
					List<ChannelFormat> channelFormats = channelFormatRepository.findByFormat(format);
					if (channelFormats != null && !channelFormats.isEmpty()) {
						logger.info("Cannot deassign format : " + format.getName()
						+ " as this channel and format has been assigned on record");
						throw new UnprocessableEntityException("Cannot deassign format : " + format.getName()
								+ " as this channel and format has been assigned on record");
					}
				}
			});
			channel.setFormats(channelDTO.getFormats());
		}
		logger.info("Returning successfully after saving channel.");
		return new ChannelDTO(channelRepository.save(channel), "forChannel");

	}

	public Channel findChannelByName(String name) {
		return channelRepository.findByNameIgnoreCase(name);
	}

	private void validateChannelFormats(Set<Format> formatDTOs) {
		formatDTOs.forEach(format -> {
			formatService.getFormatById(format.getId());

		});
	}

	private void validateChannelSave(ChannelDTO channelDto) {
		logger.info("Inside validateChannelSave() :: validating save/update channel : " + channelDto);
		if (StringUtils.isBlank(channelDto.getName())) {
			logger.info("Channel name can not be empty.");
			throw new UnprocessableEntityException("Channel name can not be empty.");
		}
		if (channelDto.getFormats() == null || channelDto.getFormats().isEmpty()) {
			logger.info("Please add atleast one format");
			throw new UnprocessableEntityException("Please add atleast one format");
		}
		Channel channelByName = findChannelByName(channelDto.getName());
		if (channelDto.getId() == null) {
			if (channelByName != null) {
				logger.info("Channel Name already exist");
				throw new UnprocessableEntityException("Channel with  given name already exist.");
			}
		} else {
			if (channelByName != null && channelByName.getId().longValue() != channelDto.getId().longValue()) {
				logger.info("Channel Name already exist");
				throw new UnprocessableEntityException("Channel with  given name already exist.");
			}
		}
		logger.info("Returning from validateChannelSave");
	}

	/**
	 * To get list of channels by format
	 * 
	 * @param format
	 * 
	 * @return list of channel
	 */
	public List<Channel> getChannelByFormat(Format format) {
		logger.info("Inside ChannelService :: , To get list of channels by format : " + format);
		List<Channel> channelsByFormat = channelRepository.findChannelByFormatName(format);
		logger.info("Returning after getting list of channel by format");
		return channelsByFormat;
	}

}
