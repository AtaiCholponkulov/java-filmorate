package ru.yandex.practicum.filmorate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@SneakyThrows
	@Test
	void validFilmValidation() {
		String validFilm = "{\"id\":null,\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":\"2015-03-01\",\"duration\":100}";
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(validFilm))
				.andExpect(status().isOk());

	}

	@SneakyThrows
	@Test
	void invalidNameFilmValidation() {
		String invalidNameFilm = "{\"id\":null,\"name\":null,\"description\":\"Film description\",\"releaseDate\":\"2015-03-01\",\"duration\":100}";
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(invalidNameFilm))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void invalidDescriptionFilmValidation() {
		String invalidDescriptionFilm = "{\"id\":null,\"name\":\"Film Name\",\"description\":\"" + "a".repeat(201) + "\",\"releaseDate\":\"2015-03-01\",\"duration\":100}";
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(invalidDescriptionFilm))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void invalidReleaseDateFilmValidation() {
		String invalidReleaseDateFilm = "{\"id\":null,\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":null,\"duration\":100}";
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(invalidReleaseDateFilm))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void invalidDurationFilmValidation() {
		String invalidDescriptionFilm = "{\"id\":null,\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":\"2015-03-01\",\"duration\":-100}";
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(invalidDescriptionFilm))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void validUserValidation() {
		String validUser = "{\"id\":null,\"email\":\"asdfasdf@asdf.com\",\"login\":\"kalyi\",\"name\":\"beka\",\"birthday\":\"2015-03-01\"}";
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(validUser))
				.andExpect(status().isOk());
	}

	@SneakyThrows
	@Test
	void invalidEmailUserValidation() {
		String invalidEmailUser = "{\"id\":null,\"email\":\"asdfasdf@asdf\",\"login\":\"kalyi\",\"name\":\"beka\",\"birthday\":\"2015-03-01\"}";
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(invalidEmailUser))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void invalidLoginUserValidation() {
		String invalidLoginUser = "{\"id\":null,\"email\":\"asdfasdf@asdf.com\",\"login\":null,\"name\":\"beka\",\"birthday\":\"2015-03-01\"}";
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(invalidLoginUser))
				.andExpect(status().is(400));
	}

	@SneakyThrows
	@Test
	void nullNameUserValidation() {
		String nullNameUser = "{\"id\":null,\"email\":\"asdfasdf@asdf.com\",\"login\":\"kalyi\",\"name\":null,\"birthday\":\"2015-03-01\"}";
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(nullNameUser))
				.andExpect(status().isOk());
	}

	@SneakyThrows
	@Test
	void invalidBirthdayUserValidation() {
		String invalidBirthdayUser = "{\"id\":null,\"email\":\"asdfasdf@asdf.com\",\"login\":\"kalyi\",\"name\":\"beka\",\"birthday\":\"2200-03-01\"}";
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(invalidBirthdayUser))
				.andExpect(status().is(400));
	}
}
