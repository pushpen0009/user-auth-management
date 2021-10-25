package com.pct.auth.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pct.auth.dto.PermissionDto;
import com.pct.auth.entity.PermissionEntity;
import com.pct.auth.entity.User;
import com.pct.auth.repository.UserRepository;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userDao;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new HashSet<>());
	}

	public List<PermissionDto> getAllPermissionsByUsername(String username) {
		User user = userDao.findByUsername(username);
		List<PermissionDto> permissionDtoList = new ArrayList<PermissionDto>();
		for(PermissionEntity permission : user.getRole().getPermissions()) {
			PermissionDto permissionDto = modelMapper.map(permission, PermissionDto.class);
			permissionDtoList.add(permissionDto);
		}
		return permissionDtoList;
	}
}
