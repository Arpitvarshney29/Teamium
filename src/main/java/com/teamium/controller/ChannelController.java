package com.teamium.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.ChannelDTO;
import com.teamium.service.ChannelService;

@RestController
@RequestMapping("/channel")
public class ChannelController {

	private ChannelService channelService;

	/**
	 * @param channelService
	 */
	public ChannelController(ChannelService channelService) {
		this.channelService = channelService;
	}

	/**
	 * To save and update channel.
	 * 
	 * Service URL:/channel method: POST
	 * 
	 * @param ChannelDTO the ChannelDTO .
	 */

	@PostMapping
	public ChannelDTO saveOrUpdateChannel(@RequestBody ChannelDTO channelDto) {
		return channelService.saveOrUpdateChannel(channelDto);
	}

	/**
	 * To get channel by id
	 * 
	 * Service URL: /channel/{customerId} method: GET.
	 * 
	 * @param channelId the channelId
	 * 
	 * @return the ChannelDTO
	 */
	@GetMapping("/{channelId}")
	public ChannelDTO getChannel(@PathVariable long channelId) {
		return channelService.getChannel(channelId);
	}

	/**
	 * To get list of channels
	 * 
	 * Service URL: /channel method: GET.
	 * 
	 * @return list of ChannelDTO
	 */
	@GetMapping
	public List<ChannelDTO> getChannels() {
		return channelService.getChannels();
	}

	/**
	 * To delete channel by id
	 * 
	 * Service URL: /channel/{customerId} method: DELETE.
	 * 
	 * @param channelId the channelId
	 */
	@DeleteMapping("/{channelId}")
	public void deleteChannel(@PathVariable long channelId) {
		channelService.deleteChannel(channelId);
	}

}
