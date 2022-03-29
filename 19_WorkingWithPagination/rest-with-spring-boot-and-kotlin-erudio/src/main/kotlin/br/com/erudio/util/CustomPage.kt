package br.com.erudio.util

import org.springframework.data.domain.Page
import org.springframework.hateoas.Link

class CustomPage<T>(page: Page<T>, link: Link, pag: CustomPageable) {
  var content: List<T>
  private var pageable: CustomPageable
  private var links: Link

  init {
    content = page.content
    pageable = pag
    links = link
  }
}