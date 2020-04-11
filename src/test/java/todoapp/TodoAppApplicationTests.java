package todoapp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import todo.api.model.criteria.SearchCriteria;
import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.StatusType;
import todo.api.repository.TodoRepository;
import todo.api.service.TodoService;
import todo.configuration.MessageConfig;
import todo.configuration.TodoAppApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TodoAppApplication.class, MessageConfig.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { "dev" })
public class TodoAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TodoService todoService;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Before
	public void setup() throws Exception {
		assertNotNull(mockMvc);
		assertNotNull(todoService);
		assertNotNull(todoRepository);
		
		todoService.saveContents("완료테스트");
		todoService.saveContents("TODO 일정1");
		todoService.saveContents("TODO 일정2");
		todoService.saveContents("TODO 일정3");
	}
	
	/**
	 * 일정 등록 테스트
	 * @throws Exception
	 */
	@Test
	public void test001_save() throws Exception {
		mockMvc.perform(post("/api/v1/todo")
							.param("contents", "등록 테스트")
						)
				.andExpect(status().isOk());
	}
	
	/**
	 * 사전에 저장된 일정 키워드 조회 테스트 
	 * @throws Exception
	 */
	@Test
	public void test002_search() throws Exception {
		SearchCriteria searchCriteria = SearchCriteria.builder()
													.page(1)
													.keyword("TODO 일정")
													.build();
		Page<TodoTuple> searchTodoList = todoService.getSearchTodoList(searchCriteria);
		int numberOfElements = searchTodoList.getNumberOfElements();

		mockMvc.perform(
			      get("/api/v1/todo")
			      .param("page", String.valueOf(searchCriteria.getPage())) 
			      .param("keyword", searchCriteria.getKeyword())
			    )
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.result.numberOfElements", is(numberOfElements)))
			.andDo(print());
	}
	
	/**
	 * 일정 수정 테스트 
	 * @throws Exception
	 */
	@Test
	public void test003_edit() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		TodoTuple tuple = TodoTuple.builder()
				.id("2")
				.contents("TODO 일정1 (edited)")
				.status(StatusType.ING)
				.insertDate(new Date())
				.build();
		
		mockMvc.perform(put("/api/v1/todo")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(tuple))
						)
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	/**
	 * 일정 변경 테스트 
	 * @throws Exception
	 */
	@Test
	public void test004_complete() throws Exception {
		mockMvc.perform(put("/api/v1/complete/1")
							.contentType(MediaType.APPLICATION_JSON)
						)
				.andDo(print())
				.andExpect(status().isOk());
		
		assertEquals(todoRepository.findById("1").get().getStatus(), StatusType.COMPLETED);
	}

}
